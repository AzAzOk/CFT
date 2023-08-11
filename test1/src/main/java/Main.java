import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static ArrayList<String> file = new ArrayList<>();
    static List<String> list = new ArrayList<>();
    static List<Integer> fileInt = new ArrayList<>();

    public static void main(String[] args) {
        ArrayList<String> inputFiles = new ArrayList<>();
        String outputFileName = null;

        // проверяем, что первый аргумент - "Start"
        if (args.length == 0 || !args[0].equals("Start")) {
            System.out.println("Ошибка: первым аргументом должно быть 'Start'.");
            return;
        }

        if (!args[1].equals("-s") && !args[1].equals("-i")) {
            System.out.println("Ошибка: второй аргумент должен быть '-s' или '-i' ");
            return;
        }

        // проверяем, что есть аргументы "-i" или "-s" и "out.txt"
        boolean hasInputFlag = false;
        for (int i = 1; i < args.length; i++) {
            if (args[i].equals("-i") || args[i].equals("-s")) {
                if (hasInputFlag) {
                    System.out.println("Ошибка: нельзя использовать одновременно флаги '-i' и '-s'.");
                    return;
                }
                hasInputFlag = true;
            } else if (args[i].equals("out.txt")) {
                if (outputFileName != null) {
                    System.out.println("Ошибка: выходной файл уже указан.");
                    return;
                }
                outputFileName = args[i];
            } else if (args[i].endsWith(".txt")) {
                File inputFile = new File(args[i]);
                if (!inputFile.exists()) {
                    System.out.println("Предупреждение: файл " + args[i] + " не найден и будет удаленн.");
                    args[i] = " ";
                } else {
                    inputFiles.add(args[i]);
                }
            }
        }
        if (outputFileName == null) {
            System.out.println("Ошибка: не указан выходной файл 'out.txt'.");
            return;
        }
        if (inputFiles.size() == 0) {
            System.out.println("Ошибка: не найдены файлы входных данных.");
            return;
        } else {
            Iterator<String> iterator = inputFiles.iterator();
            while (iterator.hasNext()) {
                String inputFile = iterator.next();
                File file = new File(inputFile);
                if (!file.exists()) {
                    System.out.println("Предупреждение: файл " + inputFile + " не найден.");
                    iterator.remove();
                }
            }
        }

        // проверяем, что файл выходных данных пустой или не существует
        File outputFile = new File(outputFileName);
        if (outputFile.exists() && outputFile.length() > 0) {
            System.out.println("Ошибка: выходной файл " + outputFileName + " уже существует и не пустой.");
            return;
        }

        // все проверки пройдены, можно запускать программу
        System.out.println("Программа успешно запущена с аргументами:");
        for (String arg : args) {
            System.out.print(arg + " ");
        }
        System.out.println("\b");
        ArrayList<String> vvod = new ArrayList<>(Arrays.asList(args));
        vvod.remove(" ");

        List<String> lines = new ArrayList<>();
        int sum = 0;
        for (String s : inputFiles) {
            sum ++;
            try (BufferedReader br = new BufferedReader(new FileReader(s))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            List<String> filteredLines = new ArrayList<>();
            for (String line : lines) {
                boolean isDigitOnly = true;
                for (char c : line.toCharArray()) {
                    if (!Character.isDigit(c) && c != ' ') {
                        isDigitOnly = false;
                        break;
                    }
                }
                if (isDigitOnly) {
                    filteredLines.add(line);
                }
            }

            filteredLines.removeIf(String::isEmpty); // удаляем пустые строки

            if (filteredLines.size() == 0) {
                System.out.println("Ошибка: файл пуст или все строки содержат неверные данные.");
            } else if (sum == inputFiles.size()) {
                file.addAll(filteredLines);
            } else {
                list.add(filteredLines.toString());
            }
        }

        if (canConvertAllStringsToInts(file)) {
            for (String s : file) {
                fileInt.add(Integer.parseInt(s));
            }
            int[] wordsArray = new int[fileInt.size()];

            for (int i = 0; i < fileInt.size(); i++) {
                wordsArray[i] = fileInt.get(i);
            }
            pop(wordsArray, args);

        }
    }
    public static void pop(int[] arr,String[] args) {
        mergeSort(arr, 0, arr.length - 1);
        if (args[1].equals("-s")) {
            Arrays.sort(arr);
            String a=Arrays.toString(arr);
            String agutin[] = a.substring(1,a.length()-1).split(", ");
            for (String i : agutin) {
                try (Writer writer = new FileWriter(args[2])) {
                    for (String line : agutin) {
                        writer.write("\"" + line + "\"");
                        writer.write(System.getProperty("line.separator"));
                    }
                    writer.flush();
                } catch (Exception e) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } else {
            Arrays.sort(arr);
            String a=Arrays.toString(arr);
            String agutin[] = a.substring(1,a.length()-1).split(", ");
            for (String i : agutin) {
                try (Writer writer = new FileWriter(args[2])) {
                    for (String line : agutin) {
                        writer.write(line);
                        writer.write(System.getProperty("line.separator"));
                    }
                    writer.flush();
                } catch (Exception e) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }
    }

    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(arr, left, middle);
            mergeSort(arr, middle + 1, right);
            merge(arr, left, middle, right);
        }
    }

    public static void merge(int[] arr, int left, int middle, int right) {
        int[] temp = new int[right - left + 1];
        int i = left;
        int j = middle + 1;
        int k = 0;

        while (i <= middle && j <= right) {
            if (arr[i] < arr[j]) {
                temp[k] = arr[i];
                i++;
            } else {
                temp[k] = arr[j];
                j++;
            }
            k++;
        }

        while (i <= middle) {
            temp[k] = arr[i];
            i++;
            k++;
        }

        while (j <= right) {
            temp[k] = arr[j];
            j++;
            k++;
        }

        for (k = 0; k < temp.length; k++) {
            arr[left + k] = temp[k];
        }
    }


    public static boolean canConvertAllStringsToInts(Collection<String> collection) {
        for (String str : collection) {
            try {
                Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }


}