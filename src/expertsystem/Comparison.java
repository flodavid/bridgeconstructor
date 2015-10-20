package expertsystem;

public class Comparison extends Word{
	private Operators op;
	private float valCondition;
	
	/**
	 * Constructeur
	 * @author florian
	 * 
	 * @param varName
	 * @param varValue
	 * @param _op
	 * @param _valCondition
	 */
	public Comparison(String varName, Operators _op, float _valCondition){
		super(varName);
		op= _op;
		valCondition= _valCondition;		
	}

	/**
	 * Constructeur avec variable déjà existante
	 * @author florian
	 * 
	 * @param varName
	 * @param varValue
	 * @param _op
	 * @param _valCondition
	 * @deprecated
	 */
//	public Comparison(Variable _var, Operators _op, int _valCondition){
//		var= _var;
//		op= _op;
//		valCondition= _valCondition;		
//	}
	
//	#########################
//	######	Getters	#########
//	#########################
	
	/**
	 * Retour sous forme de chaine de la classe
	 */
	public String toString(){
		if (op==Operators.inf)	return name + /*+"("+ var.val() +")*/" < "+ valCondition ;
		else if (op==Operators.sup)	return name + " > "+ valCondition ;
		else if (op==Operators.equal)	return name + " = "+ valCondition ;
		else if (op==Operators.sup_equal)	return name + " >= "+ valCondition ;
		else if (op==Operators.inf_equal)	return name + " <= "+ valCondition ;
		
		return "ERROR_OP";
	}
	
	/**
	 * TODO A redéfinir pour que contains fasse la bonne vérification
	 * @param other
	 * @return
	 */
//	public boolean equals(Comparison other){
//		if (this.var.equals(other.var) ) {
//			
//			if (op==Operators.inf){
//				
//			}else if (op==Operators.sup){
//			
//			}else if (op==Operators.equal){
//			
//			}else if (op==Operators.sup_equal){
//			
//			}else if (op==Operators.inf_equal){
//			
//			}
//			
//		}
//		return false;
//	}

	/**
	 * Retourne si la comparaison est exacte ou non
	 * @author florian
	 */
	public boolean isTrue(int valeurVariable) {
		if (op==Operators.inf){
			if (valeurVariable < valCondition)	return true;
		}else if (op==Operators.sup){
			if (valeurVariable > valCondition)	return true;
		}else if (op==Operators.equal){
			if (valeurVariable == valCondition)	return true;
		}else if (op==Operators.sup_equal){
			if (valeurVariable >= valCondition)	return true;
		}else if (op==Operators.inf_equal){
			if (valeurVariable <= valCondition)	return true;
		}
		
//			Cas par défaut
		return false; 
	}

	public boolean equals(Word other) {
		return false;
	}
	
	public boolean equals(Comparison other){
		System.out.println("Comparaison entre 2 comparisons");
		return (name == other.name && op == other.op && valCondition == other.valCondition);
	}
}
