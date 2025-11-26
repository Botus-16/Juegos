import javax.swing.*;
import java.math.BigDecimal;
import java.awt.event.ActionListener;

public class Calculadora {
    //Elementos de de intefaz
    private JPanel panel1;
    private JButton button0;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton buttonPlus;
    private JButton buttonMinus;
    private JButton buttonTimes;
    private JButton buttonBy;
    private JButton buttonPercent;
    private JButton buttonDot;
    private JButton buttonEquals;
    private JTextPane textPane1;

    //Variables de calculadora
    //Proximo numero a insertar
    private BigDecimal firstOperand = BigDecimal.ZERO;
    // Almacena el operador (+, -, *, /) que está esperando el segundo número
    private String operator = "";
    //Almacena el número actualmente visible en la pantalla
    private StringBuilder currentInput = new StringBuilder("0");
    //Indica si el siguiente dígito debe comenzar un nuevo número
    private boolean isNewNumber = true;

    //Funciones
        public Calculadora() {
            // Asegura que el display muestre "0" al iniciar
            textPane1.setText(currentInput.toString());

            //Configuracion de listeners
            //Funcion para manejar los listeners de numeros.
            ActionListener numberListener = e -> numberClicked(((JButton) e.getSource()).getText());

            button0.addActionListener(numberListener);
            button1.addActionListener(numberListener);
            button2.addActionListener(numberListener);
            button3.addActionListener(numberListener);
            button4.addActionListener(numberListener);
            button5.addActionListener(numberListener);
            button6.addActionListener(numberListener);
            button7.addActionListener(numberListener);
            button8.addActionListener(numberListener);
            button9.addActionListener(numberListener);

            //Funcion para manejar los listeners de operadores
            ActionListener operatorListener = e -> operatorClicked(((JButton) e.getSource()).getText());

            buttonPlus.addActionListener(operatorListener);
            buttonMinus.addActionListener(operatorListener);
            buttonTimes.addActionListener(operatorListener);
            buttonBy.addActionListener(operatorListener);

            //Listener para el Botón de Igual
            buttonEquals.addActionListener(e -> equalsClicked());

            //Listener para el Punto Decimal
            buttonDot.addActionListener(e -> dotClicked());
        }

        // ==========================================================
        // MÉTODOS DE LÓGICA (A implementar)
        // ==========================================================

        private void numberClicked(String digit) {
            if (isNewNumber) {
                // Si es un nuevo número (o después de '=', '+', etc.), reemplazar "0"
                currentInput.setLength(0); // Limpia el StringBuilder
                isNewNumber = false;
            }
            // Evita agregar más de un cero al inicio (ej: 005)
            if (currentInput.isEmpty() && digit.equals("0")) {
                currentInput.append("0");
            } else {
                currentInput.append(digit);
            }
            textPane1.setText(currentInput.toString());
        }

        private void dotClicked() {
            // Asegura que solo haya un punto decimal
            if (currentInput.indexOf(".") == -1) {
                if (isNewNumber) {
                    // Si es un nuevo número, empezar con "0."
                    currentInput.setLength(0);
                    currentInput.append("0");
                    isNewNumber = false;
                }
                currentInput.append(".");
                textPane1.setText(currentInput.toString());
            }
        }

        private void operatorClicked(String newOperator) {
            // 1. Si ya hay un operador pendiente, resolver la operación anterior
            if (!operator.isEmpty() && !isNewNumber) {
                equalsClicked();
            }

            // 2. Tomar el número actual como el primer operando
            firstOperand = new BigDecimal(currentInput.toString());

            // 3. Guardar el nuevo operador y prepararse para el segundo número
            operator = newOperator;
            isNewNumber = true;
        }

        private void equalsClicked() {
            if (operator.isEmpty()) {
                return; // No hay nada que calcular
            }

            // 1. Obtener el segundo operando (el número actualmente en pantalla)
            BigDecimal secondOperand = new BigDecimal(currentInput.toString());
            BigDecimal result = BigDecimal.ZERO;

            // 2. Realizar el cálculo basado en el operador
            switch (operator) {
                case "+":
                    result = firstOperand.add(secondOperand);
                    break;
                case "-":
                    result = firstOperand.subtract(secondOperand);
                    break;
                case "x": // o "*"
                    result = firstOperand.multiply(secondOperand);
                    break;
                case "/":
                    // Manejar división por cero y establecer precisión para el resultado
                    if (secondOperand.compareTo(BigDecimal.ZERO) == 0) {
                        textPane1.setText("Error: Division por cero");
                        resetCalculator(); // Limpia el estado
                        return;
                    }
                    // Usar MathContext para definir precisión y modo de redondeo
                    result = firstOperand.divide(secondOperand, 10, java.math.RoundingMode.HALF_UP);
                    break;
            }

            // 3. Mostrar el resultado y preparar la calculadora para la siguiente operación
            currentInput = new StringBuilder(result.toPlainString());
            textPane1.setText(currentInput.toString());

            // El resultado se convierte en el nuevo firstOperand si el usuario sigue operando
            firstOperand = result;
            operator = ""; // Limpiar el operador
            isNewNumber = true; // El siguiente dígito comenzará un nuevo cálculo
        }

        private void resetCalculator() {
            firstOperand = BigDecimal.ZERO;
            operator = "";
            currentInput = new StringBuilder("0");
            isNewNumber = true;
        }
    }
