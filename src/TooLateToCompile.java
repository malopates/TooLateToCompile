import extensions.CSVFile;
import extensions.File;

class TooLateToCompile extends Program {

int langue = 0; //0 français, 1 anglais, 2 chinois

//======================================================================================
    final CSVFile NARRATEUR = loadCSV("ressources/dialogues/narrateur.csv",';');
    final CSVFile CORLE = loadCSV("ressources/dialogues/Corle.csv",';');
    final CSVFile CONTROLEUR = loadCSV("ressources/dialogues/Controleur.csv",';');
    final CSVFile MARSHALLNORMAND = loadCSV("ressources/dialogues/MarshallNormand.csv",';');
    final CSVFile SEC = loadCSV("ressources/dialogues/Sec.csv",';');
    final CSVFile MENU = loadCSV("ressources/textes/menu.csv",';');
    final CSVFile OUTRO = loadCSV("ressources/textes/outro.csv",';');
    final CSVFile QPROGRAMMATION = loadCSV("ressources/questions/programmation.csv",';');
    final CSVFile QMATHS = loadCSV("ressources/questions/maths.csv",';');
    final CSVFile QWEB = loadCSV("ressources/questions/web.csv",';');
    final File TITRE = newFile("ressources/titre.txt");
    final File SCONTROLEUR = newFile("ressources/sprites/controleur.txt");
    final File SCARLE = newFile("ressources/sprites/controleur.txt");
    final File SSEC = newFile("ressources/sprites/sec.txt");
    final CSVFile DESCRIPTIONS = loadCSV("ressources/textes/description.csv",';');
    CSVFile save = loadCSV("ressources/save.csv");
    Joueuse player ;
    Ennemi controleur ;
    Ennemi corle ;
    Ennemi sec ;
    Ennemi marshallNormand ;
    String etape = "outro";  
    boolean skipIntro = false;


    void algorithm(){
          

            //demander le nom de la joueuse
            clearScreen();
            for (int i = 0; i < 3; i++) {
                anim(getCell(NARRATEUR,1,i),10);
                println();
            }

            initialisationTypes();
            String nom = readString(); 
            player.nom = nom;

            etape = "menu";

            if(equals(etape,"menu")){ //pour le menu, choix de langue, +1 quand changement langue, modulo 3
                menu();
            }
            
	    clearScreen();


            if(equals(etape,"Debut")){
		if(!skipIntro) introJoueuse();
		cursor(0,0);
		etape = "Controleur";
	    }


            
            if(equals(etape,"Controleur")){
                if(!skipIntro) introEnnemi(controleur);
                interfaceCombat(controleur);
            }

            if(equals(etape,"Corle")){
		etape = "Sec";
            }
	    
            if(equals(etape,"Sec")){
                 if(!skipIntro){
                    //introEnnemi(sec);
                    introSec();
                }
                interfaceCombat(sec);
            }

            if(equals(etape,"MarshallNormand")){
                
            }

            if(equals(etape,"Pause")){
                
            }

            if(equals(etape,"Combat")){
                
            }

            if(equals(etape,"Fin")){
               lancerOutro(langue); 
            }

    
    }




//===================================== INTRODUCTION =====================================  
//la variable texte est pour éviter de recopier le texte 2 fois entre anim et effacerAnim

    

    void introSec(){
        String texte = "";
        clearScreen();
        print( ANSI_RED + "yann.secq@bouleau08$ " + ANSI_WHITE);
        delay(1500);
        print(ANSI_CURSOR_SHOW);
        texte = "ijavac TooLateToCompile.java";
        anim(texte,120);
        println(ANSI_RED + "\n IndexError: index out of bounds" + ANSI_WHITE);
        print( ANSI_RED + "yann.secq@bouleau08$ " + ANSI_WHITE);
        delay(1500);
        texte = "PIGNOUF";
        anim(texte,30);
        delay(500);
        effacerAnim(texte,30);
        delay(1000);
        texte = "ijava TooLateToCompile \n";
        anim(texte,90);
        delay(2000);
        clearScreen();

    }

    void lancerOutro(int langue){
	clearScreen();
        String texte = "";
        int idxLigne = 0;
        hide();
        texte = getCell(OUTRO,idxLigne,langue);
        cursor(25,0);
        anim(texte,50);
        idxLigne++;
        effacerAnim(getCell(OUTRO,idxLigne,langue),150);
        idxLigne++;
        texte = getCell(OUTRO,idxLigne,langue);
        anim(texte,50);
        delay(1000);
        idxLigne++;
        effacerAnim(getCell(OUTRO,idxLigne,langue),20);
        //idxLigne++;
        texte = getCell(OUTRO,idxLigne,langue);
        anim(texte,30);
        delay(1000);
        effacerAnim(texte,20);
        effacerAnim("Ré",20);//les émojis gênent le comptage length donc il reste 2 caractères non-effacés
        print(ANSI_WHITE);
    }

//======================================================================================
//===================================== ANIMATIONS ===================================== 


//affiche lettre à lettre le contenu, à la vitesse pace
    void anim(String contenu, int pace){
        for(int i = 0; i<length(contenu);i++){
            delay(pace);    
            print(charAt(contenu,i));
        }
    }

//efface le texte mis en option d'un texte déjà print
    void effacerAnim(String aEffacer, int pace){
        for(int i = 0; i<length(aEffacer);i++){
            delay(pace);  
            backward();  
            print(" ");
            backward(); 
        }
    }

    void bonhommeQuiCourt(){
        for(int i = 0; i<10;i++){
            cursor(0,0);
            print(" \\O/" + "\n" + "  |_" + "\n" + " / /");
            delay(50);
            cursor(0,0);
            print("    " + "\n" + "    " + "\n" + "    ");
            cursor(0,0);
            print(" \\O/" + "\n" + " _| " + "\n" + "  \\");
            delay(50);
            cursor(0,0);
        }
    }


//===================================== MENU ===================================== 

    

    //trouve le nombre de caractère du contenu le plus grand du CSV, dans la langue choisie
    int plusGrandCSV(int langue){
        int nbPlusGrand =0;
        for(int i=0;i<rowCount(MENU)-1;i++){ //-1 car sinon encadre le texte à la fin du CSV 
            if(length(getCell(MENU,i,langue))>nbPlusGrand){
                nbPlusGrand=length(getCell(MENU,i,langue));
            }
        }
        return nbPlusGrand;
    }

    void afficherCases(int langue, CSVFile csv){
    for(int i =0;i<(rowCount(csv)-1);i++){
                    println(ANSI_RED+"╓"+barre(langue)+"╖");
                    anim(" " + ANSI_WHITE + espace(getCell(csv,i,langue),12,langue) + ANSI_RED + " \n",2);
                    println(ANSI_RED+"╙"+barre(langue)+"╜" + ANSI_WHITE);
        }
       println(getCell(MENU,rowCount(csv)-1,langue));
        
    }


    //génère la barre du cadre, de la taille de la plus grande chaîne de caractère du CSV
    String barre(int lang){
        String barreString = "";
        for(int i = 0; i<plusGrandCSV(lang);i++){
            barreString = barreString + "-";
        }
        return barreString;
    }


    //définit l'espace pour centrer chaque élément contenu dans leur cadre
    String espace (String contenu,int nbEspace,int langue){
        if(langue!=2){//les caractères chinois sont relativement petits, pas besoin d'espace
            int espaces = plusGrandCSV(langue)-length(contenu);
            for(int j =0;j<espaces/2;j++){
                contenu = " " + contenu + " ";
            }
        }
        return contenu;
    }

    void menu(){ //affiche le menu si étape = menu, sinon affiche titre
        String titreStr = "";
        for(int i=0;i<19;i++){
                titreStr = titreStr + readLine(TITRE) + "\n"; //parce que je sais pas comment remettre un curseur à 0 sur 
                //un readLine, autrement je pourrais le print qu'une fois
                }

        println(titreStr);
        afficherCases(langue,MENU);
        int selection = 0;

        while(equals(etape,"menu")){
            selection=readInt();
                    
            switch(selection){
                    case 1: //jouer
                        etape = "Debut";
                        break;

                    case 2: //langue
                        langue++;
                        langue = langue%3; //modulo 3 car 3 langues et je suis pas fan d'out of bounds sur mes csv
                        println(titreStr);
                        afficherCases(langue,MENU);
                        break;

                    case 3: //save
                        clearScreen();
                        menuSave();
                        afficherCases(langue, MENU);
                        break;

                    case 4: //Changer skip intro
                        clearScreen();
			skipIntro = !skipIntro;
                        if(skipIntro) println(getCell(NARRATEUR,13,langue));
                        else println(getCell(NARRATEUR,14,langue));
                        afficherCases(langue,MENU);
                        break;

                    case 5: //quit
                        etape="Fin";
                        break;
                        }
                    }
    }

//=============================== GESTION SAVE =====================================
    String[][] newSave;
	void creerSave(){
		// modifie la valeur du csv à partir de son indice de colonne.

		anim(getCell(NARRATEUR,15,langue),20);
		println();

		
        String[] sauvegarde = {player.indiceSave + "", player.nom, player.etape, player.score + ""}; //"attributs" du joueur

		int nbSave = rowCount(save); 

		for (int i = 0; i < rowCount(save); i++) {  //parcours toutes les lignes du CSV et remplis le tableau avec
			for (int j = 0; j < 5; j++) {
				newSave[i][j] = getCell(save, i, j); 
			}
		}


       
        print("NOMBRE SAVE " + rowCount(save) + "...");
		for (int i = 0; i < rowCount(save); i++) { //ajoute la nouvelle sauvegarde à la fin du tableau
            print(sauvegarde[i]);
			newSave[nbSave][i] = sauvegarde[i];
		}

         saveCSV(newSave, "ressources/save.csv"); //sauvegarde le tableau dans le CSV
         print("NOMBRE SAVE " + rowCount(save) + "...");
    	}

	

	void supprimerSave(){
		anim(getCell(NARRATEUR,17,langue),20);
	}

	void chargerSave(int selectionSave){
		anim(getCell(NARRATEUR,16,langue),20);
		//charge la sauvegarde choisie
		player.indiceSave = selectionSave;
		player.nom = getCell(save,selectionSave,0);
		player.etape = getCell(save,selectionSave,3);
		player.score = stringToInt(getCell(save,selectionSave,4));
		player.progression = getCell(save,selectionSave,5);
		player.multiplicateur = stringToInt(getCell(save,selectionSave,6));
		
	}

	void menuSave(){
        int nbSave = rowCount(save);
        newSave = new String[rowCount(save)+1][columnCount(save)]; //crée un tableau de la taille du CSV et laisse un espace
        loadCSV("ressources/save.csv");
		String saisie;

        	for (int i = 0; i < rowCount(save); i++) {  //parcours toutes les lignes du CSV et remplis le tableau avec
                for (int j = 0; j < 5; j++) {
                    newSave[i][j] = getCell(save, i, j); 
                    print(newSave[i][j]);
                    if(i==rowCount(save)){
                        newSave[i][j] = "VIDE";
                    }
			}
            
		}

	//print le menu
		for(int i = 0; i<length(newSave,1)-1;i++){
			println(ANSI_RED+"╓"+barre(langue)+"╖");
            print(i + "LE CHIFFRE \n" + length(newSave,1) + " LE LENGTH \n");
			anim(" " + ANSI_WHITE + espace(newSave[i][0],12,langue) + ANSI_RED + " \n",10);
			anim(" " + ANSI_WHITE + espace(newSave[i][1],12,langue) + ANSI_RED + " \n",10);
			anim(" " + ANSI_WHITE + espace(newSave[i][3],12,langue) + ANSI_RED + " \n",10);
			anim(" " + ANSI_WHITE + espace(newSave[i][4],12,langue) + ANSI_RED + " \n",10);
			println(ANSI_RED+"╙"+barre(langue)+"╜" + ANSI_WHITE);
		}
		anim(getCell(NARRATEUR,18,langue),20);

		//demande saisie
		do {
			println();
			saisie = readString();	
			saisie = toLowerCase(saisie);
		}
	//tant que la saisie n'est pas une des lettres attendues	
        while(!equals(saisie,"s") && !equals(saisie,"d") && !equals(saisie,"c") && !equals(saisie,"q"));


		switch(saisie){
			case "s":
				creerSave();
				menuSave();
				break;
			case "d":
				anim(getCell(NARRATEUR,19,langue),20);
				println();
				supprimerSave();
				println();
				menuSave();
				break;
			case "c":
				boolean valeurCorrecte = false;
				String selectionString;
				int selectionSave;
				anim(getCell(NARRATEUR,20,langue),20);
				do{ 
					selectionString = readString();
					selectionSave = stringToInt(selectionString);
					if(selectionSave >= 1 || selectionSave <= rowCount(save)) valeurCorrecte = true;
				}
				while(!valeurCorrecte);
				
				chargerSave(selectionSave);
				menuSave();
				break;

			case "q":
				break;
		}
		
	}


//=============================== GESTION COMBAT =================================


    Question newQuestion (String question, String reponse1, String reponse2, String reponse3, String reponse4, int idxReponse, int idxQuestion ){
    //CSVFile type
    
            Question q = new Question();
            q.question = question;
            q.reponse1 = reponse1;
            q.reponse2 = reponse2;
            q.reponse3 = reponse3;
            q.reponse4 = reponse4;
            q.idxReponse = idxReponse;
            q.idxQuestion = idxQuestion;
            //q.type = type;
            return q;
        }
    
    Joueuse newJoueuse(int IndiceSave,String nom,String etape, int score, String progression, int multiplicateur){
            Joueuse j = new Joueuse();
            j.indiceSave = rowCount(save);
	        j.nom = nom;
            j.etape = etape;
            j.score = 0;
	        j.multiplicateur = 1;
            return j;
        }
    
    Ennemi newEnnemi(String nom,int tauxJauge,int idxDialogue,String nomJauge,CSVFile dialogue,CSVFile questions,File sprite,int longueurSprite,String description){
            Ennemi en = new Ennemi();
            en.nom = nom;
            en.tauxJauge = tauxJauge;
            en.idxDialogue = idxDialogue;
            en.nomJauge = nomJauge;
            en.dialogue = dialogue;
            en.questions = questions;
            en.sprite = sprite;
            en.longueurSprite = longueurSprite;
            en.description = description;
            return en;
        }
    
    void initialisationTypes(){
        controleur = newEnnemi("Contrôleur", 0, 0,"Amende", CONTROLEUR, QPROGRAMMATION, SCONTROLEUR,25,getCell(DESCRIPTIONS,0,langue));
        corle = newEnnemi("M.Corle", 0, 0,"Hémmoragie", CORLE, QWEB,SCARLE,22,getCell(DESCRIPTIONS,1,langue));
        sec = newEnnemi("Sec", 0, 0,"Quota", SEC, QMATHS,SSEC,22,getCell(DESCRIPTIONS,2,langue));
        marshallNormand = newEnnemi("Marshall-Normand", 0, 0,"Note", MARSHALLNORMAND, QPROGRAMMATION,SCARLE,22,getCell(DESCRIPTIONS,3,langue));
        player = newJoueuse(1,"pardéfo","menu",0,"",1);

    }

    int currentQuestion = 0;
    void changeQuestion(Ennemi ennemi){
       
    }

    char remplirCasesJauge(Ennemi ennemi, int caseJauge){
        if(ennemi.tauxJauge<caseJauge){
            return '█';
        }else{
            return ' ';
        }
    }

    void interfaceCombat(Ennemi ennemi){
        cadreHautBas(ennemi);
            //PANNEAU GAUCHE DU COMBAT
        for(int i = 0;i<ennemi.longueurSprite;i++){
            print("║|" + ANSI_RED + remplirCasesJauge(ennemi,i) + ANSI_WHITE + "|║");
            print("║|" + readLine(ennemi.sprite) + "|║");
            if(i == 0){print("   " + ANSI_RED + player.nom + ANSI_WHITE + " ------------------- " + player.score);}
            //PANNEAU DROIT DU COMBAT
            if(i == 4){print("   " + ANSI_RED + ennemi.nom + ANSI_WHITE );} 
            if(i == 5){print("   " + substring(ennemi.description,0,length(ennemi.description)/2));} //parce que l'écran de ma machine est tout petit 
            if(i == 6){print("   " + substring(ennemi.description,length(ennemi.description)/2,length(ennemi.description)));}
            if(i == 14){print("   " + ANSI_ITALIC + getCell(NARRATEUR,4,langue) + ANSI_RED + ennemi.nomJauge + ANSI_ITALIC + ANSI_WHITE + getCell(NARRATEUR,5,langue) + ANSI_RESET);} 
            if(i == 9){print("   " + getCell(ennemi.questions,currentQuestion,langue*6));}//moitié d'une question (pas la place autrement)
            if(i == 9){print("");}//autre moitié
            if(i == 11){print("   " + "1 - " + getCell(ennemi.questions,currentQuestion,(langue*6)+1) + "                          " + "2  -" + getCell(ennemi.questions,currentQuestion,(langue*6)+2)  );}
            if(i == 13){print( "   " + "3 - " + getCell(ennemi.questions,currentQuestion,(langue*6)+3) + "                          " + "4  -" + getCell(ennemi.questions,currentQuestion,(langue*6)+4));}
            if(i != ennemi.longueurSprite-1){println();} //pour éviter de faire une ligne de vide en bas
        }
        cadreHautBas(ennemi);
    }

    void cadreHautBas(Ennemi ennemi){
        println("");
        print(ANSI_RED + toUpperCase(ennemi.nomJauge) + ANSI_WHITE);

            for(int j = 0; j<(ennemi.longueurSprite*3)-1;j++){
               print("="); 
            } 

        for(int j = 0; j<(ennemi.longueurSprite-length(ennemi.nomJauge))*3;j++){
               print("="); 
            } 
        println();
    }


//========================== SCENARIOS =================================

String gestionDialogue(Ennemi ennemi, int idxDialogue){
    print(ANSI_RED + getCell(ennemi.dialogue,0,langue) + " : " + ANSI_WHITE);
    if(idxDialogue==0){ //si procédure appelée avec 0 en paramètre, passe juste à la ligne suivante
    ennemi.idxDialogue++;
    }else{
        ennemi.idxDialogue = idxDialogue;//sinon rend la ligne demandée en paramètre
    }
    ennemi.idxDialogue = ennemi.idxDialogue % (rowCount(ennemi.dialogue)); //reviens au début du CSV au cas où l'idx dépasse
    return getCell(ennemi.dialogue,ennemi.idxDialogue,langue);
}

    void introJoueuse(){
        cursor(0,0);  
        for(int i = 0; i<2;i++){
            anim(getCell(NARRATEUR,6+i,langue),20);
            println();
            delay(2000);
        }
        clearScreen();
        bonhommeQuiCourt();
        clearScreen();  
        anim(getCell(NARRATEUR,9,langue),20);
	delay(2000);
	
    }


void introEnnemi(Ennemi ennemi){
    clearScreen();
    switch(ennemi.nom){
        case "Contrôleur" : 

           for(int i = 1; i<5;i++){
                anim(gestionDialogue(ennemi,0),20);  
                println();
                delay(2000);
            }    
        
        case "M.Corle" : 

            

        case "Sec" :

        case "Marshall-Normand" :

    }
   
 
}





}
