package de.hsrm.mi.web.projekt.validierung;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BuntValidator implements ConstraintValidator<Bunt, String> {

    @Override
    public boolean isValid(String inputColor, ConstraintValidatorContext context) {
        if(inputColor == null || inputColor.equals("")) {
            return true;
        } else if(inputColor.matches("^#[0-9a-fA-F]{3}$")) {
            String lowerInCol = inputColor.toLowerCase();
            return (lowerInCol.charAt(1) != lowerInCol.charAt(2)) && (lowerInCol.charAt(1) != lowerInCol.charAt(3)) && (lowerInCol.charAt(2) != lowerInCol.charAt(3));
        } else if(inputColor.matches("^#[0-9a-fA-F]{6}$")) {
            String lowerInCol = inputColor.toLowerCase();
            return (!lowerInCol.substring(1,3).equals(lowerInCol.substring(3,5)) && !lowerInCol.substring(1,3).equals(lowerInCol.substring(5,7)) && !lowerInCol.substring(3,5).equals(lowerInCol.substring(5,7)));
        }

        return false;
    }
}
