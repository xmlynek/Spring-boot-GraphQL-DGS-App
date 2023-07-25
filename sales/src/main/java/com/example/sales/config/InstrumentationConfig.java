package com.example.sales.config;

import com.course.graphql.generated.DgsConstants;
import graphql.GraphQLError;
import graphql.execution.ResultPath;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.fieldvalidation.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

@Configuration
public class InstrumentationConfig {

    private BiFunction<FieldAndArguments, FieldValidationEnvironment, Optional<GraphQLError>> ageMustBetween18to70() {
        return (fieldAndArguments, fieldValidationEnvironment) -> {
            Map<String, Object> argCustomer = fieldAndArguments.getArgumentValue(DgsConstants.MUTATION.CUSTOMERCREATE_INPUT_ARGUMENT.Customer);
            var birthDate = (LocalDate) argCustomer.getOrDefault(DgsConstants.CUSTOMERCREATEREQUEST.BirthDate, LocalDate.now());

            var age = ChronoUnit.YEARS.between(birthDate, LocalDate.now());

            return (age < 18 || age > 70) ?
                    Optional.of(fieldValidationEnvironment.mkError("Age must be between 18-70! " + age + " is not valid!")) :
                    Optional.empty();
        };
    }

    private BiFunction<FieldAndArguments, FieldValidationEnvironment, Optional<GraphQLError>> emailMustNotBeGmail() {
        return (fieldAndArguments, fieldValidationEnvironment) -> {
            Map<String, Object> argCustomer = fieldAndArguments.getArgumentValue(DgsConstants.MUTATION.CUSTOMERCREATE_INPUT_ARGUMENT.Customer);
            var email = (String) argCustomer.get(DgsConstants.CUSTOMERCREATEREQUEST.Email);

            return StringUtils.containsIgnoreCase(email, "gmail.com") ?
                    Optional.of(fieldValidationEnvironment.mkError("Gmail is not allowed! " + email + " is not valid!")) :
                    Optional.empty();
        };
    }

    private BiFunction<FieldAndArguments, FieldValidationEnvironment, Optional<GraphQLError>> customerUniqueInputValidation(String argumentName) {
        return (fieldAndArguments, fieldValidationEnvironment) -> {
            Map<String, Object> argCustomer = fieldAndArguments.getArgumentValue(argumentName);

            var email = (String) argCustomer.get(DgsConstants.CUSTOMERUNIQUEINPUT.Email);
            var uuid = (String) argCustomer.get(DgsConstants.CUSTOMERUNIQUEINPUT.Uuid);

            if (StringUtils.isAllBlank(email, uuid)) {
                return Optional.of(fieldValidationEnvironment.mkError("One of the customer UUID or email must exist"));
            } else if (StringUtils.isNoneBlank(email, uuid)) {
                return Optional.of(fieldValidationEnvironment.mkError("Only one parameter in CustomerUniqueInput is allowed, not both"));
            }

            return Optional.empty();
        };
    }


    @Bean
    public Instrumentation ageValidationInstrumentation() {
        ResultPath path = ResultPath.parse("/" + DgsConstants.MUTATION.CustomerCreate);
        SimpleFieldValidation fieldValidation = new SimpleFieldValidation();

        fieldValidation.addRule(path, ageMustBetween18to70());

        return new FieldValidationInstrumentation(fieldValidation);
    }

    @Bean
    public Instrumentation emailValidationInstrumentation() {
        ResultPath path = ResultPath.parse("/" + DgsConstants.MUTATION.CustomerCreate);
        SimpleFieldValidation fieldValidation = new SimpleFieldValidation();

        fieldValidation.addRule(path, emailMustNotBeGmail());

        return new FieldValidationInstrumentation(fieldValidation);
    }

    @Bean
    public Instrumentation customerUniqueInputValidationInstrumentation() {
        ResultPath queryCustomerPath = ResultPath.parse("/" + DgsConstants.QUERY.Customer);
        ResultPath addressCreatePath = ResultPath.parse("/" + DgsConstants.MUTATION.AddressCreate);

        SimpleFieldValidation simpleFieldValidation = new SimpleFieldValidation();

        simpleFieldValidation.addRule(queryCustomerPath, customerUniqueInputValidation(DgsConstants.QUERY.CUSTOMER_INPUT_ARGUMENT.Customer));
        simpleFieldValidation.addRule(addressCreatePath, customerUniqueInputValidation(DgsConstants.MUTATION.ADDRESSCREATE_INPUT_ARGUMENT.Customer));

        return new FieldValidationInstrumentation(simpleFieldValidation);
    }
}
