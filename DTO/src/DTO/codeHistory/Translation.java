package DTO.codeHistory;

import java.util.Objects;

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

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Translation that = (Translation) o;
                return Double.compare(that.time, time) == 0 && Objects.equals(input, that.input) && Objects.equals(output, that.output);
        }

        @Override
        public int hashCode() {
                return Objects.hash(input, output, time);
        }
}
