package DTO.codeHistory;

public class Translation {
        private String input;
        private String output;
        private double time;

        public Translation(String input, String output, double time) {
                this.input = input;
                this.output = output;
                this.time = time;
        }

        public String getInput() {
                return input;
        }

        public String getOutput() {
                return output;
        }
        public double getTime() {
                return time;
        }
        @Override
        public String toString() {
                StringBuilder sb = new StringBuilder();
                sb.append("<");
                sb.append(input);
                sb.append("> --> <");
                sb.append(output);
                sb.append("> (");
                sb.append(time);
                sb.append(")");
                return sb.toString();
        }
}
