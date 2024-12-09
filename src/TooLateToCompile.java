import extensions.CSVFile;
import extensions.File;

class TooLateToCompile extends Program {

//======================================================================================
//===================================== VARIABLES GLOBALES =====================================  

    final CSVFile DIALOGUES = loadCSV("dialogues.csv",';');
    final CSVFile CSVMENU = loadCSV("menu.csv",';');
    final CSVFile CSVOUTRO = loadCSV("outro.csv",';');
    final File TITRE = newFile("titre.txt");
    
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
            if(equals(etape,"menu")){
                for(int i=0;i<12;i++)
                println(readLine(TITRE));
                afficherCasesMenu(selection,langue);
                
            }
            if(equals(etape,"jeu")){
                
            }
        //lancerOutro(langue);
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
        
        println("\n \n");
        hide();
        texte = getCell(CSVOUTRO,idxLigne,langue);
        cursor(25,0);
        anim(texte,50);
        idxLigne++;
        effacerAnim(getCell(CSVOUTRO,idxLigne,langue),150);
        idxLigne++;
        texte = getCell(CSVOUTRO,idxLigne,langue);
        anim(texte,50);
        delay(1000);
        idxLigne++;
        effacerAnim(getCell(CSVOUTRO,idxLigne,langue),20);
        //idxLigne++;
        texte = getCell(CSVOUTRO,idxLigne,langue);
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
    for(int i =0;i<(rowCount(CSVMENU)-1);i++){
            if(i==selection){ //si élément est sélectionné, affichage différent
                    println(ANSI_RED+"╓"+barreString+"╖");
                    anim(" " + ANSI_WHITE + espace(getCell(CSVMENU,i,langue),12) + ANSI_RED + " \n",2);
                    println(ANSI_RED+"╙"+barreString+"╜" + ANSI_WHITE);
                }
                else{ //sinon affichage régulier
                    print(ANSI_BLUE);  
                    println("╓"+barreString+"╖");
                    anim(" " + ANSI_WHITE + espace(getCell(CSVMENU,i,langue),12) + ANSI_BLUE + " \n",10);
                    println("╙"+barreString+"╜"+ANSI_WHITE);
                    }
           
        }
       println(getCell(CSVMENU,rowCount(CSVMENU)-1,langue));
        
    }

    //trouve le nombre de caractère du contenu le plus grand du CSV, dans la langue choisie
    int plusGrandCSV(int langue){
        int nbPlusGrand =0;
        for(int i=0;i<rowCount(CSVMENU);i++){
            if(length(getCell(CSVMENU,i,langue))>nbPlusGrand){
                nbPlusGrand=length(getCell(CSVMENU,i,langue));
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



}
