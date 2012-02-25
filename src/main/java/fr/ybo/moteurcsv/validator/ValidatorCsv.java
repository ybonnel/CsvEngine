package fr.ybo.moteurcsv.validator;

public interface ValidatorCsv {

	void validate(String champ) throws ValidateException;
}
