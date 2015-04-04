import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Toggle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**@author Q*/
public class BattleField extends Application {
    Pane root;
    
    //Helper Lists
    ObservableList<Node> Field;
    FilteredList<Dude> Characters;
    SortedList<Dude> ReadyDudes;
    SortedList<Dude> DudesInOrder;
    
    
    //Readiness & Attack variables
    private final int readinessThreshold = 20;
    public String CurrentAttackersName;
    public String CurrentDefendersName;
    public Dude CurrentAttacker;
    public Dude CurrentDefender;
    public Attack selectedAttack;
    
    //Heroes
    public Dude Warrior;
    public Dude Ranger;
    public Dude Mage;
    public Dude Priest;
    
    //Enemies
    public Dude EnemyWarrior;
    public Dude EnemyRanger;
    public Dude EnemyMage;
    public Dude EnemyPriest;
    
    //Create background image and two buttons:
    //A red button to be shown if the selected attack
    //can't be used and a green one if it can.
    public ImageView ValidAttack;
    public ImageView InvalidAttack;
    public ImageView Background;
    
    //This is the Y coordinate for all the characters' sprites.
    //They will be 50 units down from the top of the GUI.
    public double AllY = 50;
    
    //These are the X coordinates of the sprites, which vary depending 
    //on which position the character is in.
    public double Hero1X = 500;
    public double Hero2X = 300;
    public double Hero3X = 150;
    public double Hero4X = 0;
    
    public double Enemy1X = 650;
    public double Enemy2X = 800;
    public double Enemy3X = 950;
    public double Enemy4X = 1150;
    
    public double[] XPositions;
    
    //These are markers to indicate valid attack positions
    public ImageView Valid1;
    public ImageView Valid2;
    public ImageView Valid3;
    public ImageView Valid4;
    public ImageView Valid5;
    public ImageView Valid6;
    public ImageView Valid7;
    public ImageView Valid8;
    
    public ImageView[] ValidPositions;
    
    //These are markers to indicate valid targets
    public ImageView Target1;
    public ImageView Target2;
    public ImageView Target3;
    public ImageView Target4;
    public ImageView Target5;
    public ImageView Target6;
    public ImageView Target7;
    public ImageView Target8;
    
    public ImageView[] ValidTargets;
    
    //This will display all output messages
    public ListView<String> MessageBoard;
    
    
    
/*//////////////////////////////////////////////////////
START
 Start, not main, is called when the program starts up
//////////////////////////////////////////////////////*/
    
    @Override
    public void start(Stage primaryStage) {
        
        
        //Panes store all the GUI elements
        root = new Pane();
        Field = root.getChildren(); //This gets used a lot. Better just
                                    //shorten it to something like "Field"
        
        
        //Initialize everything
        SetStage();
        CreateHeroes();
        CreateEnemies();
        EnterPlayers();
        FillHelperLists();
        AddListeners();
        

        //Create a scene with all the elements
        Scene scene = new Scene(root, 1300, 700);
        
        
        //Display the scene on our stage
        primaryStage.setTitle("Death BATTTLLLLEEEE!!!!!!");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        //BEGIN THE BATTLE
            PickReadiestDude();
    }

 
    
    
/*//////////////////////////////////////////////////////
SET STAGE
 Initialize the background, valid attack icons, & attack buttons 
//////////////////////////////////////////////////////*/
 
    public void SetStage(){
        
        //Initialize background, valid position/target icons, & attack button images
        Background = new ImageView(new Image("Images/MeadowBackground.png"));
        
        ValidAttack = new ImageView(new Image("Images/AttackGreen.png"));
        InvalidAttack = new ImageView(new Image("Images/AttackRed.png"));
        
        MessageBoard = new ListView<String>();
        MessageBoard.setPrefSize(220, 120);
        
        Valid1 = new ImageView(new Image("Images/Check.png"));
        Valid2 = new ImageView(new Image("Images/Check.png"));
        Valid3 = new ImageView(new Image("Images/Check.png"));
        Valid4 = new ImageView(new Image("Images/Check.png"));
        Valid5 = new ImageView(new Image("Images/Check.png"));
        Valid6 = new ImageView(new Image("Images/Check.png"));
        Valid7 = new ImageView(new Image("Images/Check.png"));
        Valid8 = new ImageView(new Image("Images/Check.png"));
        
        Target1 = new ImageView(new Image("Images/Sword.png"));
        Target2 = new ImageView(new Image("Images/Sword.png"));
        Target3 = new ImageView(new Image("Images/Sword.png"));
        Target4 = new ImageView(new Image("Images/Sword.png"));
        Target5 = new ImageView(new Image("Images/Sword.png"));
        Target6 = new ImageView(new Image("Images/Sword.png"));
        Target7 = new ImageView(new Image("Images/Sword.png"));
        Target8 = new ImageView(new Image("Images/Sword.png"));
        
        //Store Valid positions in an array
        ValidPositions = new ImageView[] {Valid1, Valid2, Valid3, Valid4,
                                          Valid5, Valid6, Valid7, Valid8};
        
        //Store Valid targets in an array
        ValidTargets = new ImageView[] {Target1, Target2, Target3, Target4,
                                        Target5, Target6, Target7, Target8};
        
        //Tell attack button where to go on the stage
        ValidAttack.setX(600);
        ValidAttack.setY(550);
        
        InvalidAttack.setX(600);
        InvalidAttack.setY(550);
        
        //Tell message board where to go
        MessageBoard.setLayoutX(540);
        MessageBoard.setLayoutY(420);
        
        //Tell valid position icons where to go
        Valid1.setX(70);
        Valid2.setX(220);
        Valid3.setX(390);
        Valid4.setX(570);
        Valid5.setX(700);
        Valid6.setX(870);
        Valid7.setX(1040);
        Valid8.setX(1200);
        
        Valid1.setY(260);
        Valid2.setY(260);
        Valid3.setY(260);
        Valid4.setY(260);
        Valid5.setY(260);
        Valid6.setY(260);
        Valid7.setY(260);
        Valid8.setY(260);
        
        //Tell valid target icons where to go
        Target1.setX(70);
        Target2.setX(220);
        Target3.setX(390);
        Target4.setX(570);
        Target5.setX(700);
        Target6.setX(870);
        Target7.setX(1040);
        Target8.setX(1200);
        
        Target1.setY(385);
        Target2.setY(385);
        Target3.setY(385);
        Target4.setY(385);
        Target5.setY(385);
        Target6.setY(385);
        Target7.setY(385);
        Target8.setY(385);
        
        //Set icons invisible for now
        Valid1.setVisible(false);
        Valid2.setVisible(false);
        Valid3.setVisible(false);
        Valid4.setVisible(false);
        Valid5.setVisible(false);
        Valid6.setVisible(false);
        Valid7.setVisible(false);
        Valid8.setVisible(false);
        
        Target1.setVisible(false);
        Target2.setVisible(false);
        Target3.setVisible(false);
        Target4.setVisible(false);
        Target5.setVisible(false);
        Target6.setVisible(false);
        Target7.setVisible(false);
        Target8.setVisible(false);
        
        //Add all 3 to the Pane
        Field.addAll(Background, 
                     ValidAttack, InvalidAttack,
                     MessageBoard,
                     Valid1, Valid2, Valid3, Valid4, Valid5, Valid6, Valid7, Valid8,
                     Target1, Target2, Target3, Target4, Target5, Target6, Target7, Target8);
    }    
    
    
    
    
/*//////////////////////////////////////////////////////
CREATE HEROES
 Initialize all heroes
//////////////////////////////////////////////////////*/
    public void CreateHeroes(){
        
        //Create Heroes
        Warrior = new Dude("Warrior", 3, false);
        Ranger = new Dude("Ranger", 2, false);
        Mage = new Dude("Mage", 1, false);
        Priest = new Dude("Priest", 0, false);
        
        //Tell them were to go on the stage
        Warrior.setX(Hero1X);
        Warrior.setY(AllY);
        
        Ranger.setX(Hero2X);
        Ranger.setY(AllY + 30); //Set ranger lower because he is crouching
        
        Mage.setX(Hero3X);
        Mage.setY(AllY);
        
        Priest.setX(Hero4X);
        Priest.setY(AllY);
        
        
        
        //Add their status symbols to the Pane
        Field.addAll(Warrior.Blood, Warrior.Poison, Warrior.Stun,
                                  Ranger.Blood, Ranger.Poison, Ranger.Stun,
                                  Mage.Blood, Mage.Poison, Mage.Stun,
                                  Priest.Blood, Priest.Poison, Priest.Stun);
        
        //Add their attacks and corresponding radio buttons to the Pane
        Field.addAll(Warrior.Attacks, Ranger.Attacks, Mage.Attacks, Priest.Attacks);
        Field.addAll(Warrior.getAttack(1), Warrior.getAttack(2), Warrior.getAttack(3), Warrior.getAttack(4),
                                  Ranger.getAttack(1), Ranger.getAttack(2), Ranger.getAttack(3), Ranger.getAttack(4),
                                  Mage.getAttack(1), Mage.getAttack(2), Mage.getAttack(3), Mage.getAttack(4),
                                  Priest.getAttack(1), Priest.getAttack(2), Priest.getAttack(3), Priest.getAttack(4));
        
        //Add the position changing radio buttons to the Pane
        Field.addAll(Warrior.MoveBack, Warrior.MoveForward,
                                  Ranger.MoveBack, Ranger.MoveForward,
                                  Mage.MoveBack, Mage.MoveForward,
                                  Priest.MoveBack, Priest.MoveForward);
        
        //Add their nameplates to the Pane
        Field.addAll(Warrior.Stats, Ranger.Stats, Mage.Stats, Priest.Stats);
        
    }
    
    
    
    
/*//////////////////////////////////////////////////////
CREATE ENEMIES
 Initialize all enemies
//////////////////////////////////////////////////////*/
    
    public void CreateEnemies(){
        
        //Create enemies
        EnemyWarrior = new Dude("Warrior", 4, true);
        EnemyRanger = new Dude("Ranger", 5, true);
        EnemyMage = new Dude("Mage", 6, true);
        EnemyPriest = new Dude("Priest", 7, true);
        
        //Tell them where to go on the stage
        EnemyWarrior.setX(Enemy1X);
        EnemyWarrior.setY(AllY);
        
        EnemyRanger.setX(Enemy2X);
        EnemyRanger.setY(AllY + 30); //Set ranger lower because he is crouching
        
        EnemyMage.setX(Enemy3X);
        EnemyMage.setY(AllY);
        
        EnemyPriest.setX(Enemy4X);
        EnemyPriest.setY(AllY);
        
        
        
        //Add their status symbols to the Pane
        Field.addAll(EnemyWarrior.Blood, EnemyWarrior.Poison, EnemyWarrior.Stun,
                                  EnemyRanger.Blood, EnemyRanger.Poison, EnemyRanger.Stun,
                                  EnemyMage.Blood, EnemyMage.Poison, EnemyMage.Stun,
                                  EnemyPriest.Blood, EnemyPriest.Poison, EnemyPriest.Stun);
        
        //Add their attacks and the corresponding radiobuttons to the Pane
        Field.addAll(EnemyWarrior.Attacks, EnemyRanger.Attacks, EnemyMage.Attacks, EnemyPriest.Attacks);
        Field.addAll(EnemyWarrior.getAttack(1), EnemyWarrior.getAttack(2), EnemyWarrior.getAttack(3), EnemyWarrior.getAttack(4),
                                  EnemyRanger.getAttack(1), EnemyRanger.getAttack(2), EnemyRanger.getAttack(3), EnemyRanger.getAttack(4),
                                  EnemyMage.getAttack(1), EnemyMage.getAttack(2), EnemyMage.getAttack(3), EnemyMage.getAttack(4),
                                  EnemyPriest.getAttack(1), EnemyPriest.getAttack(2), EnemyPriest.getAttack(3), EnemyPriest.getAttack(4));
        
        //Add the position changing radio buttons to the Pane
        Field.addAll(EnemyWarrior.MoveBack, EnemyWarrior.MoveForward,
                                  EnemyRanger.MoveBack, EnemyRanger.MoveForward,
                                  EnemyMage.MoveBack, EnemyMage.MoveForward,
                                  EnemyPriest.MoveBack, EnemyPriest.MoveForward);
        
        //Add their nameplates to the Pane
        Field.addAll(EnemyWarrior.Stats, EnemyRanger.Stats, EnemyMage.Stats, EnemyPriest.Stats);
        
        
    }
    
    
    
    
/*//////////////////////////////////////////////////////
ENTER PLAYERS
 Add characters to the scene
//////////////////////////////////////////////////////*/
    public void EnterPlayers(){

        //Add their sprites (Dude now extends ImageView) to the Pane
        Field.addAll(Priest, Mage, Ranger, Warrior);
        
        //Add enemy sprites too
        Field.addAll(EnemyWarrior, EnemyRanger, EnemyMage, EnemyPriest);
        
        
        
    }
    
    
    
    
/*//////////////////////////////////////////////////////
FILL HELPER LISTS
 For keeping track of characters
//////////////////////////////////////////////////////*/
    public void FillHelperLists(){
        
        //Populate a list of characters so we can iterate through them
        //without having to use indices. Characters and Field share
        //the Dudes now, so any change made to one is updated in the other.
        Characters = new FilteredList(Field, e -> (e instanceof Dude));
        
        //Dudes are sorted by readiness in this list. Any changes made
        //automatically updates their positions.
        ReadyDudes = new SortedList(Characters, new ReadinessComparator());
        
        //Dudes are sorted by order on the field in this list.
        DudesInOrder = new SortedList(Characters, new PositionComparator());
        
        //The X-Positions of all Dudes are stored here for reference when changing position
        XPositions = new double[] {Hero4X, Hero3X, Hero2X, Hero1X,
                                   Enemy1X, Enemy2X, Enemy3X, Enemy4X};

    }
    
    
    
    
    public void AddListeners(){
        
        
        //Listens when user selects a new attack
        for(Dude guy : Characters){
            guy.MoveSet.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle oldToggle, Toggle newToggle) -> {
                DisplayValidPositionsAndTargets();
                TurnAttackButtonOnOff();
            });
        
            
            
        //Listens when user selects new attack target
            guy.setOnMouseClicked((MouseEvent event) -> {

                if(selectedAttack.isValidTarget(guy.getPosition())){
                    CurrentDefender.setEffect(null);
                    CurrentDefender = guy;
                    PrepareDefender();
                    TurnAttackButtonOnOff();
                }

            });
        }
        
        
        
        //Listens when the Attack button is pressed
        ValidAttack.setOnMouseClicked((MouseEvent event) -> {
            
            ProcessAttack();

        });
 
    }
    
    
    
    
/*//////////////////////////////////////////////////////
START BATTLE
//////////////////////////////////////////////////////*/

    
    
    
    public void PickReadiestDude(){
        CurrentAttacker = null;
        
        //Go through the list of Dudes, sorted by readiness.
        for(Dude guy : ReadyDudes){

            
            //First guy whose readiness exceeds the threshold gets to attack.
            if((CurrentAttacker == null) && (guy.getReadiness() >= readinessThreshold)){
                
                CurrentAttacker = guy;
                break;
            }
            
            
            //For all other Dudes, increase readiness by speed
            guy.setReadiness(guy.getReadiness() + guy.getSpeed());
        }
        
        
        
        //Retry or reset current attacker's readiness and go on.
        if(CurrentAttacker == null)
            PickReadiestDude();
        else 
        {
            //Reset Dude's readiness to 0
            CurrentAttacker.setReadiness(-7);
            
            
            //Display Dude's name on the messageboard
            if(CurrentAttacker.isEnemy())
                CurrentAttackersName = "Enemy " + CurrentAttacker.getName();
            else
                CurrentAttackersName = CurrentAttacker.getName();

            Display(CurrentAttackersName + "'s turn");

            
            
            /*Once ready guy is picked, inflict bleeding, poison, & stun effects*/
            CheckHisStatus();
        
        }
    }
        
    
    
    public void CheckHisStatus(){
                
        //Inflict Bleed Damage
        CheckBlood();
        //Inflict Poison Damage
        CheckPoison();
        
        //If attacker is stunned, skip his turn
        if(CurrentAttacker.isStunned())
        {
            CheckStun();
            CheckIfAttackerIsDead();
            PickReadiestDude();
            
        //Otherwise, make his attacks visible
        }else
            ShowHisAttacks();
    }
    
    
    
    public void ShowHisAttacks(){
        
        //Make attacks visible
        CurrentAttacker.setUpAttacker();


        /*Then display the valid targets of the selected attack*/
        DisplayValidPositionsAndTargets();
        
        
    }
    
    
    
    public void DisplayValidPositionsAndTargets(){
        
        //Set all valid position icons invisible
        for(ImageView valid : ValidPositions){
            valid.setVisible(false);
        }
        
        
        //Target icons too
        for(ImageView valid : ValidTargets){
            valid.setVisible(false);
        }
          
        
        //Get currently selected attack (Moving is accounted for)
        selectedAttack = (Attack) CurrentAttacker.MoveSet.getSelectedToggle();

        
        //Set appropriate valid position icons visible
        for(int i=0; i < 8; i++){
            if(selectedAttack.isValidAttackPosition(i))
                ValidPositions[i].setVisible(true);
        }
        
        
        //Target icons too
        for(int i=0; i < 8; i++){
            if(selectedAttack.isValidTarget(i))
                ValidTargets[i].setVisible(true);
        }
        
        
        
        /*Now set up the default target of the attack*/
        PrepareDefender();
    }
    
    
    
    public void PrepareDefender(){
        
        
        //The default defender will be the first guy
        if(CurrentDefender == null)
            CurrentDefender = (CurrentAttacker.isEnemy())?(DudesInOrder.get(3)):(DudesInOrder.get(4));
        
        
        //Get the defender's name
        if(CurrentDefender.isEnemy())
            CurrentDefendersName = "Enemy " + CurrentDefender.getName();
        else
            CurrentDefendersName = CurrentDefender.getName();
        
        
        
        //Whoever it is, give him a shadow to indicate that he's targeted
            CurrentDefender.setUpDefender();
         
            
        
        /*If position & target are valid, reveal the attack button*/
        TurnAttackButtonOnOff();
    }
    
    
    
    public void TurnAttackButtonOnOff(){
        

        //Allow moving back if not already in back spot 
        if(selectedAttack.getName().equals("MoveBack") 
           && CurrentAttacker.getPosition() != 0 
           && CurrentAttacker.getPosition() != 7)
        {
            InvalidAttack.setVisible(false);
        }
        
        
        //Allow moving forward if not already in front spot
        else if(selectedAttack.getName().equals("MoveForward") 
                && CurrentAttacker.getPosition() != 3 
                && CurrentAttacker.getPosition() != 4)
        {
            InvalidAttack.setVisible(false);
        }
        
        
        //Allow attack if in correct position, valid target is selected, & target is alive
        else if(selectedAttack.isValidAttackPosition(CurrentAttacker.getPosition()) && 
                selectedAttack.isValidTarget(CurrentDefender.getPosition()) &&
                CurrentDefender.isAlive())
        {
            InvalidAttack.setVisible(false);
        }
        
        
        //Otherwise, keep attack button invalid
        else{
            InvalidAttack.setVisible(true);
        }
    }
    
    
    
    public void ProcessAttack(){
        
        //Take care of Moving Back
        if(selectedAttack.getName().equals("MoveBack")){
            
            
            //"Back" means different things for Heroes & Enemies
            //because heroes' back-to-front is 0123 & enemies' is 7654
            int switchIndex;
            if(CurrentAttacker.isEnemy())
                switchIndex = CurrentAttacker.getPosition() + 1;
            else
                switchIndex = CurrentAttacker.getPosition() - 1;
            
            
            SwitchCharacters(CurrentAttacker.getPosition(), switchIndex);
            
            
            //Display action to messageboard
            Display(CurrentAttackersName + " moved back 1");
        }
        
        
        //Take care of Moving Forward
        else if(selectedAttack.getName().equals("MoveForward")){
            
            
            int switchIndex;
            if(CurrentAttacker.isEnemy())
                switchIndex = CurrentAttacker.getPosition() - 1;
            else
                switchIndex = CurrentAttacker.getPosition() + 1;
            
            
            SwitchCharacters(CurrentAttacker.getPosition(), switchIndex);
            
            
            //Display action to messageboard
            Display(CurrentAttackersName + " moved forward 1");
        }
        
        
        //Process normal damage calculations
        else if(AttackHits(selectedAttack.getAccuracy())){
            StringBuffer AttackSummary = new StringBuffer("");
            
            int minAttackDamage = (int) Math.ceil(CurrentAttacker.getMinDamage() * (selectedAttack.getDamage() / 100.0));
            int maxAttackDamage = (int) Math.ceil(CurrentAttacker.getMaxDamage() * (selectedAttack.getDamage() / 100.0));
            int damageDealt = RandomInRange(minAttackDamage, maxAttackDamage);
            
            
            
            //If move HEALED
            if(selectedAttack.Heals()) {
                CurrentDefender.setHP(CurrentDefender.getHP() + damageDealt);

                if(CurrentDefender.getHP() > CurrentDefender.getMaxHP())
                    CurrentDefender.setHP(CurrentDefender.getMaxHP());
                
                //Display action to messageboard
                Display(CurrentAttackersName + " healed teammate\n " +
                        CurrentDefendersName + ": +" + damageDealt + "HP");
             
                
                
            //If move DAMAGED
            }else {
                CurrentDefender.setHP(CurrentDefender.getHP() - damageDealt);
                
                if(CurrentDefender.getHP() < 0) { CurrentDefender.setHP(0); }
                
                //Display action to messageboard
                AttackSummary.append(CurrentAttackersName + "'s attack hit!\n " +
                        CurrentDefendersName + ": -" + damageDealt + "HP");
                
            }
            
            
            
            //If move caused BLEEDING
            if(selectedAttack.causesBleeding()) 
            { 
                CurrentDefender.setBleeding(true);
                
                //Display action to messageboard
                AttackSummary.append("\n " + CurrentDefendersName + " started bleeding!");
            }
            
            
            
            //If move POISONED
            if(selectedAttack.Poisons()) 
            { 
                CurrentDefender.setPoisoned(true); 
                
                //Display action to messageboard
                AttackSummary.append("\n " + CurrentDefendersName + " was poisoned!");
            }
            
            
            //If move STUNNED
            if(selectedAttack.Stuns()) 
            {
                CurrentDefender.setStunned(true); 
                
                //Display action to messageboard
                AttackSummary.append("\n " + CurrentDefendersName + " was stunned!");
            }
            
            
            
            
            //If move was SHAKE FOUNDATION (We'll just hard code all this)
            if(selectedAttack.getName().equals("Shake Foundation")) 
            { 
                //Shift enemies around
                if(CurrentDefender.isEnemy()){
                    
                    //Move 1st guy to the last spot
                    SwitchCharacters(4,5);
                    SwitchCharacters(5,6);
                    SwitchCharacters(6,7);
                    
                    //Then switch the 2nd & 3rd
                    SwitchCharacters(4,5);
                    SwitchCharacters(5,6);
                    
                    //Lastly, move the last guy to the front
                    SwitchCharacters(4,5);
                    
                }
                
                //Shift heroes around
                else{
                    
                    //Move 1st guy to the last spot
                    SwitchCharacters(3,2);
                    SwitchCharacters(2,1);
                    SwitchCharacters(1,0);
                    
                    //Then switch the 2nd & 3rd
                    SwitchCharacters(3,2);
                    SwitchCharacters(2,1);
                    
                    //Lastly, move the last guy to the front
                    SwitchCharacters(3,2);
                    
                }
                
                
                //Display action to messageboard
                AttackSummary.append("\n The earth shook viciously!");
            }
            
            
            //If move was SHIELD BASH
            else if(selectedAttack.getName().equals("Shield Bash")){
                
                //The the target's position
                int targetPosition = CurrentDefender.getPosition();
                
                
                //Move the enemy target 2 spots back
                if(CurrentDefender.isEnemy())
                {
                    SwitchCharacters(targetPosition, targetPosition + 1);
                    SwitchCharacters(targetPosition + 1, targetPosition + 2);
                }
                
                
                //Move the hero target 2 spots back
                else
                {
                    SwitchCharacters(targetPosition, targetPosition - 1);
                    SwitchCharacters(targetPosition - 1, targetPosition - 2);
                }
                
                
                //Display action to messageboard
                AttackSummary.append("\n " + CurrentDefendersName + " was flung back!");
            }
            
            //Print out completed turn summary
            Display(AttackSummary.toString());
            
            //Check whether the attack killed
            CheckIfDefenderIsDead();
            
            
        }else{
            //Display miss message
            Display(CurrentAttackersName + "'s attack missed!");
        }
        
        
        
        /*Once attack is completed, go on to next ready character*/
        if(NobodyHasWonYet()){
            CurrentAttacker.hideEverything();
            CurrentDefender.setEffect(null);
            CurrentDefender = null;
            PickReadiestDude();
        }
    }
    
    
 
/*//////////////////////////////////////////////////////
HELPER METHODS
//////////////////////////////////////////////////////*/   
    
    public void Display(String message){
        MessageBoard.getItems().add(message);
    }
    
    
    public void SwitchCharacters(int a, int b){
        int indexA = DudesInOrder.getSourceIndex(a);
        int indexB = DudesInOrder.getSourceIndex(b);
        
        //Switch two characters' position variables
        Characters.get(indexB).setPosition(a);
        Characters.get(indexA).setPosition(b);
        
        //This needs to be done in order to refresh the sorted list. 
        DudesInOrder = new SortedList(Characters, new PositionComparator());
        
        //Switch their actual positions on the field
        DudesInOrder.get(a).setX(XPositions[DudesInOrder.get(a).getPosition()]);
        DudesInOrder.get(b).setX(XPositions[DudesInOrder.get(b).getPosition()]);
    }
    
    
    public boolean AttackHits(int accuracy) {
		int accuracyResult = RandomInRange(0, 100);
		if(accuracyResult <= accuracy) {
			return true;
		}
		return false;
	}
    
    
    public void CheckBlood(){
        int damage = 0;

    //Inflict Bleeding Damage
        if(CurrentAttacker.isBleeding())
        {
            damage = CurrentAttacker.BleedBabyBleed();
            Display(CurrentAttackersName + " lost blood\n  -" + damage + "HP");
        }
    }

    
    public void CheckPoison(){
        int damage = 0;
        
        //Inflict Poison Damage
        if(CurrentAttacker.isPoisoned())
        {
            damage = CurrentAttacker.FeelingVenomenal();
            Display(CurrentAttackersName + " hurt by poison\n  -" + damage + "HP");
        }

    }

    
    public void CheckStun(){

        //Skip turn
        if(CurrentAttacker.isStunned())
        {
            CurrentAttacker.WheelchairBound();
            Display(CurrentAttackersName + " is stunned");
        }


    }
    
    
    public void CheckIfAttackerIsDead(){
        
        boolean isDead = CurrentAttacker.UpdateLivingOrDead();
        
        if(isDead)
        {
            //Display dead Dude's name on the messageboard
            Display(CurrentAttackersName + " was killed ☺");
        }
    }
    
    
    public void CheckIfDefenderIsDead(){
        
        boolean isDead = CurrentDefender.UpdateLivingOrDead();
        
        if(isDead)
        {
            //Display dead Dude's name on the messageboard
            Display(CurrentDefendersName + " was killed ☺");
        }
    }
    
     
    public boolean NobodyHasWonYet(){

    //If Enemies won, return true
    if(!Warrior.isAlive() && !Ranger.isAlive() && !Mage.isAlive() && !Priest.isAlive())
    {
        Display("\n\nENEMIES WIN\n\n");
        return false;
    }

    //If Heroes won, return true
    else if(!EnemyWarrior.isAlive() && !EnemyRanger.isAlive() && !EnemyMage.isAlive() && !EnemyPriest.isAlive())
    {
        Display("\n\nHEROES WIN\n\n");
        return false;
    }

    //Otherwise, return true and keep playing
    return true;
}
    
    
    public int RandomInRange(int min, int max) {
		HighQualityRandom random = new HighQualityRandom(System.currentTimeMillis() * (max + 5));
		return (random.next(25) % (max - min + 1) + min);
	}
    
    
    
    
/*//////////////////////////////////////////////////////
MAIN
 The main method is not used in JavaFX
//////////////////////////////////////////////////////*/
    
    public static void main(String[] args) {
        launch(args);
    }
    
}

    
    
    
   
    
    
    
