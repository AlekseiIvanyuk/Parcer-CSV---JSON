import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String fileNameJson = "data.json";
        String fileNameJson2 = "data2.json";

        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        String dataJson = writeFile(json, fileNameJson);

        // TODO
        // List<Employee> list2 = parseXML("data.xml");
        //  String json2 = listToJson(list2);
        // String dataJson2 = writeFile(json2, fileNameJson2);

        //Код из презентации для разбора

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("data.xml"));
        Node root = doc.getDocumentElement();
        System.out.println("Корневой элемент: " + root.getNodeName());
        read(root);


    }
    // код из презентации, ниже мой код
    private static void read(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                System.out.println("Текущий узел: " + node_.getNodeName());
                Element element = (Element) node_;
                NamedNodeMap map = element.getAttributes();
                for (int a = 0; a < map.getLength(); a++) {
                    String attrName = map.item(a).getNodeName();
                    System.out.println(attrName);
                    String attrValue = map.item(a).getNodeValue();
                    System.out.println(attrValue);
                    System.out.println("Атрибут: " + attrName + "; значение: " + attrValue);
                }
                read(node_);
            }
        }
    }


    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
            staff.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return staff;
    }


    public static String listToJson(List<Employee> list) {


        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        System.out.println(gson.toJson(list));
        return json;
    }

    // метод записи
    public static String writeFile(String json, String fileNameJson) {
        try (FileWriter file = new
                FileWriter(fileNameJson)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

//    public static List<Employee> parseXML(String fileName) throws IOException, SAXException, ParserConfigurationException {
//        List<Employee> staff2 = new ArrayList<>();
//        DocumentBuilderFactory factory = DocumentBuilderFactory. newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        Document doc = builder.parse( new File(fileName));
//        Node root = doc.getDocumentElement();
//        System.out.println("Корневой элемент: " + root.getNodeName());
//        read(root);
//
//
//        return staff2;//TODO
//    }
//
//    private static NodeList read(Node node) {
//        NodeList nodeList = node.getChildNodes();
//        for (int i = 0; i < nodeList.getLength(); i++) {
//            Node node_ = nodeList.item(i);
//            if (Node.ELEMENT_NODE == node_.getNodeType()) {
//                System. out.println("Текущий узел: " + node_.getNodeName());
//                Element element = (Element) node_;
//                NamedNodeMap map = element.getAttributes();
//                for (int a = 0; a < map.getLength(); a++) {
//                    String attrName = map.item(a).getNodeName();
//                    String attrValue = map.item(a).getNodeValue();
//                    System. out.println("Атрибут: " + attrName + "; значение: " + attrValue);
//                }
//                read(node_);
//            }
//        }
//        return nodeList;
//    }


}
