package com.wora.waitingroom.common.application.validation;

import com.wora.waitingroom.common.application.validation.validator.ReferenceExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ReferenceExistsValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReferenceExists {
    String message() default "Given Entity Id Does Not Exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> idClass();

    Class<?> entityClass();

}
