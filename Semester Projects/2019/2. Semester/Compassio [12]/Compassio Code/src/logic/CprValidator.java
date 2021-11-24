package logic;

/**
 * The CprValidator class is a utility class for modolo 11 validating Danish
 * CPR numbers.
 * @author Morten Kargo Lyngesen
 */
public class CprValidator {

    private static final int[] CONTROL_NUMBER = {4, 3, 2, 7, 6, 5, 4, 3, 2};
    private static final int MODOLO_CONTROL = 11;

    /**
     * Returns if a given CPR number is valid.
     * @param cpr CPR number to test validity of.
     * @return <code>true</code> if given cpr number is valid, and 
     * <code>false</code> if incorrect.
     */
    public static boolean validate(String cpr) {
        if (cpr.length() != 10) {
            return false;
        }

        if (Integer.parseInt(cpr.substring(0, 2)) > 31) {
            return false;
        }
        if (Integer.parseInt(cpr.substring(2, 4)) > 12) {
            return false;
        }
        if (Integer.parseInt(cpr.substring(4, 6)) == 0) {
            return false;
        }

        int[] cprArray = new int[cpr.length()];
        for (int i = 0;
                i < cpr.length();
                i++) {
            cprArray[i] = Integer.parseInt(cpr.split("")[i]);
        }
        if (!firstControlCipher(Character.getNumericValue(cpr.charAt(6)), Integer.parseInt(cpr.substring(4, 6)))) {
            return false;
        }

        int total = 0;

        for (int i = 0;
                i < CONTROL_NUMBER.length;
                i++) {
            total += cprArray[i] * CONTROL_NUMBER[i];
        }
        int modolo = (total % MODOLO_CONTROL);
    
        int control = MODOLO_CONTROL - modolo;
        if (control > 9) {
            control = control / 10;
        }
        return (control == Character.getNumericValue(cpr.charAt(9)));
    }

    private static boolean firstControlCipher(int cipher, int year) {
        switch (cipher) {
            case 0:
            case 1:
            case 2:
            case 3:
                return true;
            case 4:
            case 9:
                if (year < 37) {
                    return true;
                }
            case 5:
            case 6:
            case 7:
            case 8:
                return year < 37 || year > 57;
            default:
                return false;
        }
    }
}
