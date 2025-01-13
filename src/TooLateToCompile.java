import extensions.CSVFile;
import extensions.File;

class TooLateToCompile extends Program {

int langue = 0; //0 français, 1 anglais, 2 chinois

//======================================================================================
    final CSVFile NARRATEUR = loadCSV("ressources/dialogues/narrateur.csv",';');
    final CSVFile ALMEIDADODO = loadCSV("ressources/dialogues/Corle.csv",';');
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
    final CSVFile DESCRIPTIONS = loadCSV("ressources/textes/description.csv",';');
    CSVFile save = loadCSV("ressources/save.csv",';');
    Joueuse player ;
    Ennemi controleur ;
    Ennemi corle ;
    Ennemi sec ;
    Ennemi marshallNormand ;
    String etape = "outro";  


    void algorithm(){
          
          
          boolean skipIntro = false;
            
            for (int i = 0; i < 3; i++) {
                anim(getCell(NARRATEUR,0,i),10);
                println();
            }

             //demander si l'on veut skip l'intro
            String readS = readString();
            if(equals(readS,"oui") || equals(readS,"yes") || equals(readS,"y") || equals(readS,"o") || equals(readS,"是")){
                skipIntro = true;
            }else{
                clearScreen();
                introJoueuse();
            }
            cursor(0,0);

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
            initialisationTypes();


            if(equals(etape,"Debut")){}


            etape = "Controleur";
            if(equals(etape,"Controleur")){
                if(!skipIntro){
                    introEnnemi(controleur);
                }
                interfaceCombat(controleur);
            }

            if(equals(etape,"Corle")){

            }

            if(equals(etape,"Sec")){
                introSec();
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

    void afficherCasesMenu(int langue ){
    for(int i =0;i<(rowCount(MENU)-1);i++){
                    println(ANSI_RED+"╓"+barre(langue)+"╖");
                    anim(" " + ANSI_WHITE + espace(getCell(MENU,i,langue),12,langue) + ANSI_RED + " \n",2);
                    println(ANSI_RED+"╙"+barre(langue)+"╜" + ANSI_WHITE);
        }
       println(getCell(MENU,rowCount(MENU)-1,langue));
        
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
        afficherCasesMenu(langue);
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
                        afficherCasesMenu(langue);
                        break;

                    case 3: //save
                        clearScreen();
                        println("j'ai pas encore codé ce truc");
                        afficherCasesMenu(langue);
                        break;

                    case 4: //quit
                        etape="Fin";
                        clearScreen();
                        break;
                        }
                    }
    }


//=============================== GESTION COMBAT =================================
    int currentQuestion = 0;
    
    Joueuse newJoueuse(String nom,int IndiceSave,String etape, int points){
            Joueuse j = new Joueuse();
            j.nom = nom;
            j.indiceSave = IndiceSave;
            j.etape = etape;
            j.points = 0;
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
        sec = newEnnemi("Sec", 0, 0,"Quota", SEC, QMATHS,SCARLE,22,getCell(DESCRIPTIONS,2,langue));
        marshallNormand = newEnnemi("Marshall-Normand", 0, 0,"Note", MARSHALLNORMAND, QPROGRAMMATION,SCARLE,22,getCell(DESCRIPTIONS,3,langue));
        player = newJoueuse("pardéfo",0,"menu",0);

    }


    void changeQuestion(Ennemi ennemi){
        currentQuestion = (int) (random()*rowCount(ennemi.questions)); 
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
            print("║|" + readLine(SCONTROLEUR) + "|║");
            if(i == 0){print("   " + ANSI_RED + player.nom + ANSI_WHITE + " ------------------- " + player.points);}
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
            anim(getCell(NARRATEUR,6+i,langue),50);
            println();
            delay(1000);
        }
        clearScreen();
        bonhommeQuiCourt();
        clearScreen();  
        anim(getCell(NARRATEUR,9,langue),50);
    }


//void introEnnemi(Ennemi){
void introEnnemi(Ennemi ennemi){
    clearScreen();
   
    for(int i = 1; i<4;i++){
      anim(gestionDialogue(ennemi,0),50);  
      println();
      delay(1000);
    }    
}





}
