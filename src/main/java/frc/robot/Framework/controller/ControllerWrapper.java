package frc.robot.framework.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class ControllerWrapper{

    private ControllerBase controller;
    private Map<String, SubsystemCollection> subsystemCollections = new HashMap<>();
    //private XMLParser parser = new XMLParser();
    private class SubsystemCollection{
        public Map<String, String> buttons = new HashMap<>();
        public Map<String, String> axes = new HashMap<>();
        private Element subsystemElement;

        public SubsystemCollection(Element system){
            subsystemElement = system;
            NodeList buttonNodes = system.getElementsByTagName("button");
            for(int i = 0; i < buttonNodes.getLength(); i++){
                Node currentButton = buttonNodes.item(i);
                if(currentButton.getNodeType() == Node.ELEMENT_NODE){
                    Element buttonElement = (Element)currentButton;
                    buttons.put(buttonElement.getAttribute("command"), buttonElement.getAttribute("button"));
                }
            }

            NodeList axisNodes = system.getElementsByTagName("axis");
            for(int i = 0; i < axisNodes.getLength(); i++){
                Node currentAxis = axisNodes.item(i);
                if(currentAxis.getNodeType() == Node.ELEMENT_NODE){
                    Element axisElement = (Element)currentAxis;
                    axes.put(axisElement.getAttribute("command"), axisElement.getAttribute("axis"));
                }
            }
        }

        public String getAttribute(String attribute){
            return subsystemElement.getAttribute(attribute);
        }
    }

    public ControllerWrapper(ControllerBase controllerType, Element controllerXML){
        controller = controllerType;
        NodeList subsystems = controllerXML.getChildNodes();
        for(int i = 0; i < subsystems.getLength(); i++){
            Node currentSubsystem = subsystems.item(i);
            if(currentSubsystem.getNodeType() == Node.ELEMENT_NODE){
                Element systemElement = (Element)currentSubsystem;
                subsystemCollections.put(systemElement.getTagName(), new SubsystemCollection(systemElement));
            }
        }
    }
    
    public boolean getButton(String buttonName, String subsystemName){
        SubsystemCollection requestedSystem = subsystemCollections.get(subsystemName);
        if(requestedSystem == null){
            controllerError("Button", buttonName, subsystemName);
            return false;
        }
        String requestedButton = requestedSystem.buttons.get(buttonName);
        if(requestedButton == null){
            controllerError("Button", buttonName, subsystemName);
            return false;
        }
        return controller.getButton(requestedButton);
    }

    public double getAxis(String axisName, String subsystemName){
        SubsystemCollection requestedSystem = subsystemCollections.get(subsystemName);
        if(requestedSystem == null){
            controllerError("Axis", axisName, subsystemName);
            return 0.0;
        }
        String requestedAxis = requestedSystem.axes.get(axisName);
        if(requestedAxis == null){
            controllerError("Axis", axisName, subsystemName);
            return 0.0;
        }
        return controller.getAxis(requestedAxis);
    }

    public String getAttribute(String attribute, String subsystemName){
        SubsystemCollection requestedSystem = subsystemCollections.get(subsystemName);
        if(requestedSystem == null){
            System.out.println("Attribute:" + attribute + " not found.");
        }
        return requestedSystem.getAttribute(attribute);
    }
    
    static List<String> errorAry = new ArrayList<>();
    private void controllerError(String type, String id, String subsystemName){
        boolean found = false;
        for(var i = 0; i< errorAry.size() ; i++){
            
            if(errorAry.get(i) == id){
                found = true;
            }
        }
        if(found == false){
            System.out.println(type +  ":" + id + " not found. Subsystem:"+subsystemName +" not registered on requested controller.");
            errorAry.add(id);
        }
        
    }
}