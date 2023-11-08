package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCard.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * CardFactory class implements the factory method pattern for the creation of the Character Cards.
 * All the information about the cards and their effects are stored in a xml resource file.
 * This class will parse that xml and get all the information for creating the right card.
 *
 * @author Christian Lisi
 */

public class CardFactory {

    private static final String fileName = "xml/characterCards.xml";


    /**
     * Method createCard implement the factory method.
     * Based on the param cardID it creates the right character card.
     *
     * @param cardID - the card's ID
     * @param game   type {@link Game}- the Game object
     * @return card of type {@link CharacterCard}
     */

    public CharacterCard createCard(String cardID, Game game) {
        CharacterCard card = null;

        HashMap<String, String> cardInfo = getRightCardInfo(cardID);

        //GameBoard is used to set up Character card
        switch (cardInfo.get("effect")) {
            case "OneStudentToAnIsland":
                card = new OneStudentToAnIslandCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("setup"),
                        cardInfo.get("effectDescription"), game);

                break;

            case "TakeControlOfProfessor":
                card = new TakeControlOfProfessorCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("effectDescription"));
                break;
            case "ResolveIsland":
                card = new ResolveIslandCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("effectDescription"));
                break;
            case "Add2ToMotherNatureMovement":
                card = new Add2ToMotherNatureMovementCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("effectDescription"));
                break;
            case "NoEntryCard":
                card = new NoEntryCardCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("setup"),
                        cardInfo.get("effectDescription"));
                break;
            case "NoTowerInInfluence":
                card = new NoTowerInInfluenceCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("effectDescription"));
                break;
            case "Replace3StudentsInEntrance":
                card = new Replace3StudentsInEntranceCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("setup"),
                        cardInfo.get("effectDescription"), game);
                break;
            case "TwoMoreInfluencePoint":
                card = new TwoMoreInfluencePointCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("effectDescription"));
                break;
            case "ColorNoInfluence":
                card = new ColorNoInfluenceCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("effectDescription"));
                break;
            case "Exchange2Students":
                card = new Exchange2StudentsCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("effectDescription"));
                break;
            case "Take1StudentToDiningRoom":
                card = new Take1StudentToDiningRoomCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("setup"),
                        cardInfo.get("effectDescription"), game);
                break;
            case "Remove3StudentsFromDiningRoom":
                card = new Remove3StudentsFromDiningRoomCharacterCard(
                        cardInfo.get("ID"),
                        cardInfo.get("cost"),
                        cardInfo.get("effect"),
                        cardInfo.get("setup"),
                        cardInfo.get("effectDescription"));
                break;

        }

        return card;
    }


    /**
     * Method getFileFromResource locate and return the xml file
     *
     * @return the xml file as input stream
     */
    private InputStream getFileFromResource() {

        InputStream resource = getClass().getClassLoader().getResourceAsStream("xml/characterCards.xml");

        if (resource == null) {
            System.out.println("NOT OK");
            throw new IllegalArgumentException("File not Found! ");
        } else {
            return resource;
        }
    }


    /**
     * Method getRightCardInfo return all the info parsed from the XML file about the card with the selected id.
     * <p>
     * Each xml card node has the tag <ID> <cost> <effect> <setup> and <effectDescription>.
     * This method parse the XML, extract all the info about the chosen card and store them
     * in a HashMap<String,String>
     *
     * @param id - The chosen card's id
     * @return card of type HashMap<String,String> with al the parsed card's info.
     */
    public HashMap<String, String> getRightCardInfo(String id) {

        HashMap<String, String> card = new HashMap<>();
        CardFactory cf = new CardFactory();

        InputStream xmlFile = cf.getFileFromResource();


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            // process XML securely, avoid attacks like XMLExternalEntities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlFile);

            // normalize the output of the parsed document
            doc.getDocumentElement().normalize();

            //get all card
            NodeList list = doc.getElementsByTagName("card");

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element elem = (Element) node;

                    // get card's id label
                    String parsedID = elem.getElementsByTagName("ID").item(0).getTextContent().trim();

                    // if is the right effect, collect and return all the card's info
                    if (parsedID.equals(id)) {

                        String effectType = elem.getElementsByTagName("effect").item(0).getTextContent().trim();
                        String cost = elem.getElementsByTagName("cost").item(0).getTextContent().trim();
                        String setup = elem.getElementsByTagName("setup").item(0).getTextContent().trim();
                        String effectDescription = elem.getElementsByTagName("effectDescription").item(0).getTextContent().trim();

                        card.put("ID", id);
                        card.put("cost", cost);
                        card.put("effect", effectType);
                        card.put("setup", setup);
                        card.put("effectDescription", effectDescription);

                        return card;
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return card;
    }

}
