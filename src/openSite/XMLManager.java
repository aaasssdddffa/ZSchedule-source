package openSite;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLManager {
    public static void XMLWriter(File file, String eleName, String[] nodeList, String[] contentNames, boolean AttAble, boolean[] AttValue) throws ParserConfigurationException, TransformerException {
        if(nodeList.length == contentNames.length) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement(eleName);

            for (int i = 0; i < nodeList.length; i++) {
                Element ele = document.createElement(nodeList[i]);
                ele.setTextContent(contentNames[i]);
                if(AttAble) {
                    ele.setAttribute("change", String.valueOf(AttValue[i]));
                }
                root.appendChild(ele);
            }
            document.appendChild(root);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(out);
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                if (file.isFile() && file.canWrite()) {
                    try {
                        bufferedWriter.write(out.toString(StandardCharsets.UTF_8));
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String ReadFile(NodeList children, int i, String[] arrays) {
        String changeAble;
        int index = i;
        if(index == 0) {index = 1;}
        else {index = index * 2 + 1;}
        Node node = children.item(index);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element ele = (Element) node;
            String content = ele.getTextContent();
            arrays[i] = content;
            if(ele.getAttribute("change").equals("true") || ele.getAttribute("change").equals("false")) {
                changeAble = ele.getAttribute("change");
                return changeAble;
            }
        }
        return null;
    }
    public static String ReadFile(NodeList children, int i) {
        String changeAble;
        int index = i;
        if(index == 0) {
            index = 1;
        } else {
            index = index * 2 + 1;
        }
        Node node = children.item(index);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element ele = (Element) node;
            if(ele.getAttribute("change").equals("true") || ele.getAttribute("change").equals("false")) {
                changeAble = ele.getAttribute("change");
                return changeAble;
            }
            return ele.getTextContent();
        }
        return null;
    }
    public static String XMLReader(String path, String[] Arrays, int index) throws ParserConfigurationException, SAXException, IOException {
        String Ables;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(path);
        Element root = document.getDocumentElement();
        NodeList children = root.getChildNodes();
        Ables = ReadFile(children, index, Arrays);
        return Ables;
    }
    public static String XMLReader(String path, int index) throws ParserConfigurationException, SAXException, IOException {
        String Ables;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(path);
        Element root = document.getDocumentElement();
        NodeList children = root.getChildNodes();
        Ables = ReadFile(children, index);
        return Ables;
    }
}
