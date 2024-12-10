import extensions.CSVFile;
import extensions.File;

class TooLateToCompile extends Program {

//======================================================================================

    final CSVFile ALMEIDADODO = loadCSV("dialogues/Corle.csv",';');
    final CSVFile CORLE = loadCSV("dialogues/Corle.csv",';');
    final CSVFile CONTROLEUR = loadCSV("dialogues/Controleur.csv",';');
    final CSVFile MARSHALLNORMAND = loadCSV("dialogues/MarshallNormand.csv",';');
    final CSVFile SEC = loadCSV("dialogues/Sec.csv",';');
    final CSVFile MENU = loadCSV("menu.csv",';');
    final CSVFile OUTRO = loadCSV("outro.csv",';');
    final CSVFile QPROGRAMMATION = loadCSV("questions/programmation.csv",';');
    final CSVFile QMATHS = loadCSV("questions/maths.csv",';');
    final CSVFile QWEB = loadCSV("questions/web.csv",';');
    final File TITRE = newFile("titre.txt");
    final File SCONTROLEUR = newFile("sprites/controleur.txt");
    final CSVFile DESCRIPTIONS = loadCSV("description.csv",';');

    
//======================================================================================
//===================================== MAIN =====================================  
   String etape = ""; //initialement des booleans "début" "jeu" "fin" mais au final String qui influent 
   //sur le main semble plus clair et moins compliqué
    boolean skipIntro = true; //pour que je puisse tester rapidement


    void algorithm(){

            if(!skipIntro){
                lancerIntro();
                
            }
            etape = "menu";
            interfaceCombat(controleur);
            if(equals(etape,"menu")){ //pour le menu, choix de langue, +1 quand changement langue, modulo 3
                for(int i=0;i<12;i++)
                println(readLine(TITRE));
                afficherCasesMenu(selection,langue);
                
            }
            if(equals(etape,"Controleur")){
                
            }

            if(equals(etape,"Corle")){
                
            }

            if(equals(etape,"Sec")){
                
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

    String texte = "";

    void lancerIntro(){
        clearScreen();
        print( ANSI_RED + "yann.secq@bouleau08$ " + ANSI_WHITE);
        delay(1500);
        print(ANSI_CURSOR_SHOW);
        texte = "ijavac prototypeJeuV350.java";
        anim(texte,120);
        println(ANSI_RED + "\n IndexError: index out of bounds" + ANSI_WHITE);
        print( ANSI_RED + "yann.secq@bouleau08$ " + ANSI_WHITE);
        delay(1500);
        texte = "PIGNOUF";
        anim(texte,30);
        effacerAnim(texte,30);
        delay(1000);
        texte = "ijava backup \n";
        anim(texte,90);
        delay(2000);
        clearScreen();

    }

    void lancerOutro(int langue){
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


//===================================== MENU ===================================== 

    int selection = 0; //pour si le keyTyped sera autorisé, navigation par flèches directionelles 
    int langue = 1; //0 français, 1 anglais, 2 chinois
    int plusGrand = plusGrandCSV(langue);
    String barreString ="";


    void afficherCasesMenu(int seletion,int langue ){
        String barre = barre(langue);
    for(int i =0;i<(rowCount(MENU)-1);i++){
            //si élément est sélectionné, affichage différent
                    println(ANSI_RED+"╓"+barreString+"╖");
                    anim(" " + ANSI_WHITE + espace(getCell(MENU,i,langue),12) + ANSI_RED + " \n",2);
                    println(ANSI_RED+"╙"+barreString+"╜" + ANSI_WHITE);
        }
       println(getCell(MENU,rowCount(MENU)-1,langue));
        
    }

    //trouve le nombre de caractère du contenu le plus grand du CSV, dans la langue choisie
    int plusGrandCSV(int langue){
        int nbPlusGrand =0;
        for(int i=0;i<rowCount(MENU);i++){
            if(length(getCell(MENU,i,langue))>nbPlusGrand){
                nbPlusGrand=length(getCell(MENU,i,langue));
            }
        }
        return nbPlusGrand;
    }


    //génère la barre du cadre, de la taille de la plus grande chaîne de caractère du CSV
    String barre(int lang){
        for(int i = 0; i<plusGrandCSV(lang);i++){
            barreString = barreString + "-";
        }
        return barreString;
    }


    //définit l'espace pour centrer chaque élément contenu dans leur cadre
    String espace (String contenu,int nbEspace){
        int espaces = plusGrand-length(contenu);
        for(int j =0;j<espaces/2;j++){
            contenu = " " + contenu + " ";
        }
        return contenu;
    }


//=============================== GESTION COMBAT =================================

    Ennemi newEnnemi(String nom,int tauxJauge,int idxDialogue,String nomJauge,CSVFile dialogue,CSVFile questions,File sprite,int longueurSprite,String description){
        Ennemi en = new Ennemi();

        en.nom = nom;
        en.tauxJauge = tauxJauge;
        en.nomJauge = nomJauge;
        en.idxDialogue = idxDialogue;
        en.dialogue = dialogue;
        en.questions = questions;
        en.sprite = sprite;
        en.longueurSprite = longueurSprite;
        en.description = description;
        return en;
    }
    
    Ennemi controleur = newEnnemi("Contrôleur", 0, 0,"Amende", CONTROLEUR, QMATHS, SCONTROLEUR,25,getCell(DESCRIPTIONS,0,langue));
    //Ennemi sec = newEnnemi("M.Sec", 0, 0,"Hémmoragie", CORLE, QWEB, SSEC,22);
    
    int currentQuestion(Ennemi ennemi){
        return (int) (random()*rowCount(ennemi.questions)); 
    }

    void interfaceCombat(Ennemi ennemi){
        cadreHautBas(ennemi);
        for(int i = 0;i<ennemi.longueurSprite;i++){
            print("║|" + "    " + "|║");
            print("║|" + readLine(SCONTROLEUR) + "|║");
            if(i == 4){print("   " + ANSI_RED + ennemi.nom + ANSI_WHITE );} //mettre les éléments sur la droite sur l'affichage combat
            if(i == 5){print("   " + ennemi.description);} 
            if(i == 6){print("   " + "Remplissez la jauge d'" + ANSI_RED + ennemi.nomJauge + ANSI_WHITE + " à gauche pour gagner ");} 
            if(i == 9){print("   " + getCell(QMATHS,currentQuestion(ennemi),langue*6));}//moitié d'une question (pas la place autrement)
            if(i == 9){print("");}//autre moitié
            if(i == 11){print("   " + "1 - " + "Placeholder" + "                          " + "2  -" + "Placeholder" );}
            if(i == 13){print( "   " + "1 - " + "Placeholder" + "                          " + "2  -" + "Placeholder");}
            if(i != ennemi.longueurSprite-1){println();} //pour éviter de faire une ligne de vide en bas
        }
        cadreHautBas(ennemi);
    }

    void cadreHautBas(Ennemi ennemi){
        println("");
        print("✧");
        for(int i = 0; i<2;i++){
            for(int j = 0; i<ennemi.longueurSprite;i++){
               print("=-="); 
            } 
        }
        print("✧");
        println("");
    }


}
