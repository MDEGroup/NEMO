package cpb.loader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.epsilon.cbp.dto.EventJuri;
import org.eclipse.epsilon.cbp.event.AddToEAttributeEvent;
import org.eclipse.epsilon.cbp.event.AddToEReferenceEvent;
import org.eclipse.epsilon.cbp.event.ChangeEvent;
import org.eclipse.epsilon.cbp.event.CreateEObjectEvent;
import org.eclipse.epsilon.cbp.event.DeleteEObjectEvent;
import org.eclipse.epsilon.cbp.event.MoveWithinEAttributeEvent;
import org.eclipse.epsilon.cbp.event.MoveWithinEReferenceEvent;
import org.eclipse.epsilon.cbp.event.RemoveFromEAttributeEvent;
import org.eclipse.epsilon.cbp.event.RemoveFromEReferenceEvent;
import org.eclipse.epsilon.cbp.event.SetEAttributeEvent;
import org.eclipse.epsilon.cbp.event.SetEReferenceEvent;
import org.eclipse.epsilon.cbp.event.UnsetEAttributeEvent;
import org.eclipse.epsilon.cbp.event.UnsetEReferenceEvent;

public class Encoder {

	private EventJuri ej;
	private Map<String, Integer> idMap;
	private int currentId;

	public Encoder(EventJuri eiu) {
		ej = eiu;
		idMap = new HashMap<>();
		currentId = 0;
	}

	public void encode(boolean numericalRepr) throws IOException {
		String s = "";
		int i = 0;
		System.out.println("populating string");
		int counter = 0;
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("dataset.csv"))) {
			for (ChangeEvent el : ej.getChangeEvents()) {
				counter++;
				if (counter % 10000 == 0)
					System.out.println("Computed " + counter);
				if (counter % 100000 == 0) {
					writer.write(s);
					s = "";
				}
				if (numericalRepr) {
					if (el instanceof CreateEObjectEvent) {
						String id = ((CreateEObjectEvent) el).getId();
						EClass clazz = ej.geteObjectMap().get(id);
						s += getIdValue("create") + "," + getIdValue("class") + "," + getIdValue(clazz.getName())
								+ "\n";
					}
					if (el instanceof SetEAttributeEvent) {
						EClass id = (EClass) ((SetEAttributeEvent) el).getTarget();
						String name = ((SetEAttributeEvent) el).getValue().toString();
						s += getIdValue("setAttr") + "," + getIdValue(id.getName()) + "," + getIdValue(name) + "\n";
					}
					if (el instanceof SetEReferenceEvent) {
						EClass id = (EClass) ((SetEReferenceEvent) el).getTarget();
						String name = ((SetEReferenceEvent) el).getName().toString();
						s += getIdValue("setRef") + "," + getIdValue(id.getName()) + "," + getIdValue(name) + "\n";
					}
					if (el instanceof AddToEReferenceEvent) {
						EClass id = (EClass) ((AddToEReferenceEvent) el).getTarget();
						String name = ((AddToEReferenceEvent) el).getName().toString();
						s += getIdValue("addRef") + "," + getIdValue(id.getName()) + "," + getIdValue(name) + "\n";
					}
					if (el instanceof RemoveFromEReferenceEvent) {
						EClass id = (EClass) ((RemoveFromEReferenceEvent) el).getTarget();
						String name = ((RemoveFromEReferenceEvent) el).getName().toString();
						s += getIdValue("remRef") + "," + getIdValue(id.getName()) + "," + getIdValue(name) + "\n";
					}
					if (el instanceof UnsetEAttributeEvent) {
						EClass id = (EClass) ((UnsetEAttributeEvent) el).getTarget();
						String name = ((UnsetEAttributeEvent) el).getName().toString();
						s += getIdValue("unsAtt") + "," + getIdValue(id.getName()) + "," + getIdValue(name) + "\n";
					}
					if (el instanceof UnsetEReferenceEvent) {
						EClass id = (EClass) ((UnsetEReferenceEvent) el).getTarget();
						String name = ((UnsetEReferenceEvent) el).getName().toString();
						s += getIdValue("unsRef") + "," + getIdValue(id.getName()) + "," + getIdValue(name) + "\n";
					}

					if (el instanceof AddToEAttributeEvent) {
						EClass id = (EClass) ((AddToEAttributeEvent) el).getTarget();
						String name = ((AddToEAttributeEvent) el).getName().toString();
						s += getIdValue("addAtt") + "," + getIdValue(id.getName()) + "," + getIdValue(name) + "\n";
					}
					if (el instanceof RemoveFromEAttributeEvent) {
						EClass id = (EClass) ((RemoveFromEAttributeEvent) el).getTarget();
						String name = ((RemoveFromEAttributeEvent) el).getName().toString();
						s += getIdValue("remAtt") + "," + getIdValue(id.getName()) + "," + getIdValue(name) + "\n";
					}
					if (el instanceof MoveWithinEAttributeEvent) {
						EClass id = (EClass) ((MoveWithinEAttributeEvent) el).getTarget();
						String name = ((MoveWithinEAttributeEvent) el).getName().toString();
						s += getIdValue("movAtt") + "," + getIdValue(id.getName()) + "," + getIdValue(name) + "\n";
					}
					if (el instanceof MoveWithinEReferenceEvent) {
						EClass id = (EClass) ((MoveWithinEReferenceEvent) el).getTarget();
						String name = ((MoveWithinEReferenceEvent) el).getName().toString();
						s += getIdValue("movRef") + "," + getIdValue(id.getName()) + "," + getIdValue(name) + "\n";
					}
					if (el instanceof DeleteEObjectEvent) {
						String id = ((DeleteEObjectEvent) el).getId();
						EClass clazz = ej.geteObjectMap().get(id);
						s += getIdValue("delete") + "," + getIdValue("class") + "," + getIdValue(clazz.getName())
								+ "\n";
					}
				} else {
					if (el instanceof CreateEObjectEvent) {
						String id = ((CreateEObjectEvent) el).getId();
						EClass clazz = ej.geteObjectMap().get(id);
						s += "create,class," + clazz.getName() + "\n";
					}
					if (el instanceof SetEAttributeEvent) {
						EClass id = (EClass) ((SetEAttributeEvent) el).getTarget();
						String name = ((SetEAttributeEvent) el).getValue().toString();
						s += "setAttr," + id.getName() + "," + name + "\n";
					}
					if (el instanceof SetEReferenceEvent) {
						EClass id = (EClass) ((SetEReferenceEvent) el).getTarget();
						String name = ((SetEReferenceEvent) el).getName().toString();
						s += "setRef," + id.getName() + "," + name + "\n";
					}
					if (el instanceof AddToEReferenceEvent) {
						EClass id = (EClass) ((AddToEReferenceEvent) el).getTarget();
						String name = ((AddToEReferenceEvent) el).getName().toString();
						s += "setRef," + id.getName() + "," + name + "\n";
					}
					if (el instanceof RemoveFromEReferenceEvent) {
						EClass id = (EClass) ((RemoveFromEReferenceEvent) el).getTarget();
						String name = ((RemoveFromEReferenceEvent) el).getName().toString();
						s += "remRef," + id.getName() + "," + name + "\n";
					}

					if (el instanceof UnsetEAttributeEvent) {
						EClass id = (EClass) ((UnsetEAttributeEvent) el).getTarget();
						String name = ((UnsetEAttributeEvent) el).getName().toString();
						s += "unsAtt," + id.getName() + "," + name + "\n";
					}
					if (el instanceof UnsetEReferenceEvent) {
						EClass id = (EClass) ((UnsetEReferenceEvent) el).getTarget();
						String name = ((UnsetEReferenceEvent) el).getName().toString();
						s += "unsRef," + id.getName() + "," + name + "\n";
					}

					if (el instanceof AddToEAttributeEvent) {
						EClass id = (EClass) ((AddToEAttributeEvent) el).getTarget();
						String name = ((AddToEAttributeEvent) el).getName().toString();
						s += "addAtt," + id.getName() + "," + name + "\n";
					}
					if (el instanceof RemoveFromEAttributeEvent) {
						EClass id = (EClass) ((RemoveFromEAttributeEvent) el).getTarget();
						String name = ((RemoveFromEAttributeEvent) el).getName().toString();
						s += "remAtt," + id.getName() + "," + name + "\n";
					}
					if (el instanceof MoveWithinEAttributeEvent) {
						EClass id = (EClass) ((MoveWithinEAttributeEvent) el).getTarget();
						String name = ((MoveWithinEAttributeEvent) el).getName().toString();
						s += "movAtt," + id.getName() + "," + name + "\n";
					}
					if (el instanceof MoveWithinEReferenceEvent) {
						EClass id = (EClass) ((MoveWithinEReferenceEvent) el).getTarget();
						String name = ((MoveWithinEReferenceEvent) el).getName().toString();
						s += "movRef," + id.getName() + "," + name + "\n";
					}
					if (el instanceof DeleteEObjectEvent) {
						String id = ((DeleteEObjectEvent) el).getId();
						EClass clazz = ej.geteObjectMap().get(id);
						s += "delete,class," + clazz.getName() + "\n";
					}
				}
				i++;

			}
			System.out.println("Writing file");

			writer.write(s);
		}

	}

	private int getIdValue(String value) {
		if (idMap.containsKey(value))
			return idMap.get(value);
		else {
			idMap.put(value, ++currentId);
			return currentId;
		}
	}
}
