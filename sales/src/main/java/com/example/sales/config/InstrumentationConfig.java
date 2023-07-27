package com.example.sales.config;

import com.course.graphql.generated.DgsConstants;
import com.course.graphql.generated.types.LoanInput;
import graphql.GraphQLError;
import graphql.analysis.MaxQueryDepthInstrumentation;
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

    private BiFunction<FieldAndArguments, FieldValidationEnvironment, Optional<GraphQLError>> emailMustNotBeGmail(String argumentName) {
        return (fieldAndArguments, fieldValidationEnvironment) -> {
            Map<String, Object> argCustomer = fieldAndArguments.getArgumentValue(argumentName);
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

    private BiFunction<FieldAndArguments, FieldValidationEnvironment, Optional<GraphQLError>> customerUpdateRequestValidation() {
        return (fieldAndArguments, fieldValidationEnvironment) -> {
            Map<String, Object> argCustomerRequest = fieldAndArguments.getArgumentValue(DgsConstants.MUTATION.CUSTOMERUPDATE_INPUT_ARGUMENT.CustomerUpdate);

            var email = (String) argCustomerRequest.get(DgsConstants.CUSTOMERUPDATEREQUEST.Email);
            var phone = (String) argCustomerRequest.get(DgsConstants.CUSTOMERUPDATEREQUEST.Phone);

            if (StringUtils.isAllBlank(email, phone)) {
                return Optional.of(fieldValidationEnvironment.mkError("One of the customer phone or email must exist in CustomerUpdateRequest!"));
            }

            return Optional.empty();
        };
    }

    private BiFunction<FieldAndArguments, FieldValidationEnvironment, Optional<GraphQLError>> requestContainsLoanWhenIsLoanValidation() {
        return (fieldAndArguments, fieldValidationEnvironment) -> {
            Map<String, Object> salesOrderArgs = fieldAndArguments.getArgumentValue(DgsConstants.MUTATION.SALESORDERCREATE_INPUT_ARGUMENT.SalesOrder);
            Map<String, Object> financeArgs = (Map<String, Object>) salesOrderArgs.get(DgsConstants.CREATESALESORDERINPUT.Finance);

            var isLoan = (boolean) financeArgs.get(DgsConstants.FINANCEINPUT.IsLoan);
            var loanArgs = financeArgs.get(DgsConstants.FINANCEINPUT.Loan);

            if (isLoan && loanArgs == null) {
                return Optional.of(fieldValidationEnvironment.mkError("Loan must not be null when 'isLoan' is true!"));
            } else if (!isLoan && loanArgs != null) {
                return Optional.of(fieldValidationEnvironment.mkError("Loan request should not exist when 'isLoan' is false!"));
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

        fieldValidation.addRule(path, emailMustNotBeGmail(DgsConstants.MUTATION.CUSTOMERCREATE_INPUT_ARGUMENT.Customer));

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

    @Bean
    public Instrumentation customerUpdateRequestValidationInstrumentation() {
        ResultPath path = ResultPath.parse("/" + DgsConstants.MUTATION.CustomerUpdate);

        SimpleFieldValidation fieldValidation = new SimpleFieldValidation();

        fieldValidation.addRule(path, customerUpdateRequestValidation());
        fieldValidation.addRule(path, emailMustNotBeGmail(DgsConstants.MUTATION.CUSTOMERUPDATE_INPUT_ARGUMENT.CustomerUpdate));

        return new FieldValidationInstrumentation(fieldValidation);
    }

    @Bean
    public Instrumentation loanValidationInstrumentation() {
        ResultPath path = ResultPath.parse("/" + DgsConstants.MUTATION.SalesOrderCreate);

        SimpleFieldValidation fieldValidation = new SimpleFieldValidation();

        fieldValidation.addRule(path, requestContainsLoanWhenIsLoanValidation());

        return new FieldValidationInstrumentation(fieldValidation);
    }
}
