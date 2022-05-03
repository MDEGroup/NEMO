package cpb.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.stream.FactoryConfigurationError;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.epsilon.cbp.dto.EventJuri;
import org.eclipse.epsilon.cbp.event.ChangeEvent;
import org.eclipse.epsilon.cbp.hybrid.xmi.CBP2XMIResource;
import org.eclipse.epsilon.cbp.resource.CBPResource;
import org.eclipse.epsilon.cbp.resource.CBPXMLResourceFactory;
import org.eclipse.epsilon.cbp.resource.JuriCBPXMLResourceImpl;
import org.eclipse.uml2.uml.Model;

import CBP.CBPPackage;
import CBP.Session;

public class Loader {

	public static void main(String[] args) throws FactoryConfigurationError, IOException {
		cpb2xmi();
//		EPackage.Registry.INSTANCE.put(CBPPackage.eNS_URI, CBPPackage.eINSTANCE);
//		ResourceSet resSet = new ResourceSetImpl();
//		Resource resource = resSet.getResource(URI.createURI("BPMN2.cbpxml"), true);
//		Session myWeb = (Session) resource.getContents().get(0);
//		System.out.println(myWeb);
	}
	
	public static void cpb2xmi() throws FactoryConfigurationError, IOException {
		
		
		
		
		JuriCBPXMLResourceImpl cbpResource = (JuriCBPXMLResourceImpl) (new CBPXMLResourceFactory()). //
				createResource(URI.createFileURI("BPMN2.cbpxml"));
			System.out.println("Computing LOs");
			EventJuri k = cbpResource.replayEvents(new FileInputStream(new File("BPMN2.cbpxml")));
			System.out.println("Compututed LOs");
			System.out.println("Encoding LOs");
			Encoder encoder = new Encoder(k);
			encoder.encode(true);
			System.out.println("Encoded LOs");

//			Model model = (Model) cbpResource.getContents().get(0);
//			System.out.println(model.getName());
	}
	
	

}
