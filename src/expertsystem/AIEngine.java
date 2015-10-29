package expertsystem;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * La classe utilisera les règles ainsi que la base de faits
 * @see Environment pour décider quel pont scierait à la base
 */

public class AIEngine {
	private RulesBase BR;
	
    /**
     *
     * @param _BR
     */
    public AIEngine(RulesBase _BR){
		BR= _BR;
	}
	
	/**
	 * On sature la BF (version de base, on boucle sur BR (sans ordre partiel))
	 * On vérifie que l'on fait au moins une inférence par cycle, sinon arrêt
	 * @author Florian
	 * 
	 * @param BR ensemble de règles
	 * @param BF ensemble de faits initiaux (Environnment)
	 * 
	 * VF(f) valeur de f dans BF
	 * VA(m, r)  affirmation ou négation du mot m dans Ant.(r), r € BR
	 * VC(m,r) affirmation ou négation du mot m dans Cons.(r), r € BR
     * @return la base de faits avec le chainage avant appliqué
	 */
	public FactsBase forwardChaining(FactsBase BF) {
		boolean inf= true;	// pour savoir si on a fait une inférence durant le cycle
		int nbInf= 0;

		System.out.println("-------------------------------------------------------");
		System.out.println("|   SATURATION DE LA BASE DE FAITS : Chainage avant   |");
		System.out.println("-------------------------------------------------------");
		
		while(inf){
			inf= false;
			// on fait une copie de la BR, on peut alors la modifier pendant le parcours de sa copie
			RulesBase BRcpy= new RulesBase(BR);
			
			for ( Rule rule : BRcpy){ // parcours de toutes les règles de la copies => Parcours en Largeur
				System.out.println("Recherche applicable : " + rule);
				
//				/*Antécédants des règles*/
				boolean dec= true;

                for (Iterator<Word> it = rule.getAntecedants().iterator() ; it.hasNext() && dec;) {
                    Word wAnt = it.next();
                    Word tmp= BF.contains(wAnt);
                    if ( tmp == null || ! wAnt.sameValue(tmp.getVal())/*(tmp.getVal()).equals(wAnt.getVal())*/ ) // VF(f)!=VA(wAnt,r)
                        dec= false;
                }
                
				if (dec){
//					On ajoute toutes les conséquences de la règle car elle est vraie
					for (Word wCons : rule.getConsequences() ) {
						BF.add(wCons);
					}
					inf= true;
					++nbInf;
					//TODO this.Mémoriser(r,nbInf) /* Pour l'explication ???*/ 
					System.out.println("\tRègle appliquée");
					BR.remove(rule); /* Une règle se déclenche au plus une fois */
				}
				
			}
		}
		System.out.println("Nb inférence chainage avant : "+ nbInf);
		return BF;
	}
	
	/**
	 * Retourne une base de règles, ces dernières ayant toutes pour conséquences au moins le fait A
	 * @param factName nom du fait
	 * @return RulesBase
	 */
	private RulesBase getRulesWithConsequent(String factName) {
		RulesBase rulesCopy = new RulesBase(BR);
		for(int i = 0 ; i < rulesCopy.size() ; ++i) {
			Rule R = rulesCopy.get(i);
			boolean b = false;
			for(Word W : R.getConsequences())
				if((W.getName()).equals(factName)) 
					b = true;
			if(!b)
				rulesCopy.remove(i);
		}
		return rulesCopy;
	}
    
    /**
	 * Retourne une base de règles, ces dernières ayant toutes pour conséquences au moins le fait A
	 * @param fact fait
	 * @return RulesBase
	 */
	private RulesBase getRulesWithConsequent(Word fact) {
        return getRulesWithConsequent(fact.getName());
    }
    
    private Word response(String goal){
            System.out.println("\n Connaissez vous la valeur de : "+ goal);
            Scanner sc; sc= new Scanner(System.in);           
            String answerValue= sc.next();
            
            System.out.println("\tEst-ce que "+ answerValue +"est correcte ? Y/N");
            boolean answer= sc.nextBoolean();
            if (answer){
                try{
                    float res= Float.parseFloat(answerValue);
                    return new Comparison(goal, Operators.equal, res);
                }catch(NumberFormatException NFE){
                    boolean res= Boolean.parseBoolean(goal);
                    return new Affirmation(goal, res);
                }
            }
            else return response(goal); // récursivité
    }
	
	
	/**
	 * Procédure VERIF
	 * @param WList ensemble de buts à vérifier tous
	 * @param FB : {@link FactsBase}
	 * @return boolean
	 */
	private boolean VERIF(List<Word> WList, FactsBase FB) {
		boolean ver = true;
		for(Word word : WList) {
			ver = backwardChaining(word.getName(), FB);
			if(ver == false) break;	
		}
		return ver;
	}    
	
	/**
	 * Chaînage Arrière - Procédure DEMO
	 * @param goal : But récursivement établi (b dans l'exemple)
	 * @param FB : La Base de Faits
	 * @return boolean 
	 */
	public boolean backwardChaining(String goal, FactsBase FB) {
		System.out.println("---------------------------------------------------------");
		System.out.println("|   SATURATION DE LA BASE DE FAITS : Chainage arrière   |");
		System.out.println("---------------------------------------------------------");
		
        Word goalFact= null;
		// La procédure devrait s'appeler DEMO...
		boolean dem = false;
		// 1er cas évident :
		if(FB.contains(goal) != null) dem = true;
        
        for (Iterator<Rule> it = getRulesWithConsequent(goal).iterator(); it.hasNext() && dem;) {
            Rule R = it.next();
            dem = VERIF(R.getAntecedants(), FB);
        }
        
		// 3ème cas : sinon voir si b est demandable
		if(dem == false && FB.isFactDemandable(goal)) {
						// Si b est demandable
			// Poser la question b ?
                // Demande à l'utilisateur s'il connait la valeur de goal :

            goalFact = response(goal); // VRAI, FAUX, ou inconnu (Pas vraiment ici)
                
                
        /// OLD
//			Word fact= FB.contains(goal);
//			if (fact != null){
//				// Alors fact n'est pas inconnu, on test si il est vrai
//				dem = ( (goal.getVal()).equals( fact.getVal() ) );
//			}
//			else dem= false; 
            
		}
		// Dans tous les cas mémoriser et ajouter à la BF
		if(dem == true)
			FB.add(goalFact);
		// return dem ?
		return dem;
	}

}
