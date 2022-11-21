package de.hsrm.mi.web.projekt.validierung;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Min;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy=BuntValidator.class)
public @interface Bunt {
    String message() default "Die Farbe enthaelt zwei gleiche R/G/B-Anteile und ist daher nicht bunt genug";
    Class<? extends Payload>[] payload() default { };
    Class<?>[] groups() default { };
}
