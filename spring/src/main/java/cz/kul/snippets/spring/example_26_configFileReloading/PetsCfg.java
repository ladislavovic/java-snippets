package cz.kul.snippets.spring.example_26_configFileReloading;

import java.util.ArrayList;
import java.util.List;

public class PetsCfg {

	private List<PetCfg> pets = new ArrayList<>();

	public List<PetCfg> getPets() {
		return pets;
	}

	public void setPets(List<PetCfg> pets) {
		this.pets = pets;
	}

	@Override
	public String toString() {
		return "PetsCfg{" +
				"pets=" + pets +
				'}';
	}

}
