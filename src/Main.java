import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        System.out.print("Введите выражение: ");
        String input = scn.nextLine();
        try {
            System.out.println(calc(input));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public static String calc(String input) throws Exception {
        Converter converter = new Converter();
        String[] actions = {"+", "-", "/", "*"};
        String[] regexActions = {"\\+", "-", "/", "\\*"};
        int actionIndex = -1;

        for (int i = 0; i < actions.length; i++) {
            if (input.contains(actions[i])) {
                actionIndex = i;
                break;
            }
        }

        if (actionIndex == -1) {
            throw new Exception("Некорректное выражение");
        }

        String[] data = input.split(regexActions[actionIndex]);
        if (data.length != 2) {
            throw new Exception("Некорректное количество операндов");
        }

        boolean isRoman = converter.isRoman(data[0]) && converter.isRoman(data[1]);
        boolean isArabic = !converter.isRoman(data[0]) && !converter.isRoman(data[1]);

        if (!isRoman && !isArabic) {
            throw new Exception("Числа должны быть в одном формате");
        }

        int a, b;
        if (isRoman) {
            a = converter.romanToInt(data[0]);
            b = converter.romanToInt(data[1]);
        } else {
            a = Integer.parseInt(data[0]);
            b = Integer.parseInt(data[1]);
        }

        if (a < 1 || a > 10 || b < 1 || b > 10) {
            throw new Exception("Числа должны быть от 1 до 10 включительно");
        }

        int result;
        switch (actions[actionIndex]) {
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;
            case "*":
                result = a * b;
                break;
            case "/":
                result = a / b;
                break;
            default:
                throw new Exception("Некорректное арифметическое действие");
        }

        if (isRoman) {
            if (result < 1) {
                throw new Exception("Результат римских чисел не может быть меньше 1");
            }
            return converter.intToRoman(result);
        } else {
            return String.valueOf(result);
        }
    }
}

class Converter {
    private final String[] roman = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C"};
    private final int[] arabian = {1, 4, 5, 9, 10, 40, 50, 90, 100};

    public boolean isRoman(String number) {
        for (String r : roman) {
            if (number.startsWith(r)) {
                return true;
            }
        }
        return false;
    }

    public String intToRoman(int number) {
        StringBuilder romanNumber = new StringBuilder();
        for (int i = arabian.length - 1; i >= 0; i--) {
            while (number >= arabian[i]) {
                romanNumber.append(roman[i]);
                number -= arabian[i];
            }
        }
        return romanNumber.toString();
    }

    public int romanToInt(String s) {
        int result = 0;
        int i = 0;

        while (i < s.length()) {
            char currentChar = s.charAt(i);
            int currentValue = valueOfRoman(currentChar);

            int nextValue = 0;
            if (i + 1 < s.length()) {
                nextValue = valueOfRoman(s.charAt(i + 1));
            }

            if (currentValue < nextValue) {
                result += nextValue - currentValue;
                i += 2;
            } else {
                result += currentValue;
                i++;
            }
        }

        return result;
    }

    private int valueOfRoman(char romanChar) {
        switch (romanChar) {
            case 'I': return 1;
            case 'V': return 5;
            case 'X': return 10;
            case 'L': return 50;
            case 'C': return 100;
            default: throw new IllegalArgumentException("Некорректный римский символ: " + romanChar);
        }
    }
}
