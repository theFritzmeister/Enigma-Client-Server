package generated;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringReader;

public class XMLParser {
    public static CTEEnigma parseXMLStringToObject(String xmlFile) throws Exception{
        try {


            JAXBContext jaxbContext = JAXBContext.newInstance(CTEEnigma.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            CTEEnigma enigma = (CTEEnigma) jaxbUnmarshaller.unmarshal(new StreamSource( new StringReader(xmlFile)));
            return enigma;

        } catch (JAXBException e) {
            throw new Exception("Couldn't open file...");
        }
    }
    public static CTEEnigma parseXMLtoObject(String fName) throws Exception{
        try {

            if(fName.charAt(0) == '"') {
                fName = fName.substring(1, fName.length() - 1);
            }

            File file = new File(fName);

            JAXBContext jaxbContext = JAXBContext.newInstance(CTEEnigma.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            CTEEnigma enigma = (CTEEnigma) jaxbUnmarshaller.unmarshal(file);
            return enigma;

        } catch (JAXBException e) {
            throw new Exception("Could'nt open '" + fName + "'");
        }
    }

    private static void fromXmlFileToObject() {
        System.out.println("\nFrom File to Object");

        try {

            File file = new File("C:\\Users\\user\\Desktop\\MTA\\year 3\\Java\\rolling project\\test files\\ex1\\ex1-sanity-small.xml");

            JAXBContext jaxbContext = JAXBContext.newInstance(CTEEnigma.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            CTEEnigma enigma = (CTEEnigma) jaxbUnmarshaller.unmarshal(file);
            System.out.println(enigma.cteMachine.getABC().trim());

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

    private static void fromObjectToXmlFile() {

        System.out.println("\nFrom Object to File");

        CTEEnigma enigma = new CTEEnigma();
        enigma.cteMachine.setABC("ABCDEF");
        System.out.println("Object toString: " + enigma.toString());
        System.out.println("Object as XML:");
        try {

            File file = new File("try.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(CTEEnigma.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(enigma, file);
            jaxbMarshaller.marshal(enigma, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
