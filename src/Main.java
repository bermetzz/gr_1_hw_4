import java.util.Random;

public class Main {
    public static int bossHealth = 700;
    public static int bossDamage = 50;
    public static String bossDefenceType;
    public static int[] heroesHealth = {250, 270, 280, 300, 900, 220, 230, 218};
    public static int[] heroesDamage = {25, 20, 15, 10, 2, 11, 21, 18};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic","Medic", "Golem", "Lucky", "Berserk", "Thor"};
    public static int roundNumber = 0;
    public static int healthCure=100;
    public static int dodgeDamage;


    public static void main(String[] args) {
        printStatistics();
        while (!isGameFinished()) {
            playRound();
        }
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        bossHits();
        heroesHit();
        docCures();
        printStatistics();
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " ---------------");
        System.out.println("Boss health: " + bossHealth + "; damage: "
                + bossDamage + "; defence: " + (bossDefenceType == null ? "No defence" : bossDefenceType));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i] + "; damage: "
                    + heroesDamage[i]);
        }
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length);
        bossDefenceType = heroesAttackType[randomIndex];
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }

        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0 ) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
        }
        return allHeroesDead;
    }

    public static void bossHits() {
        Random random=new Random();
        boolean luckyChance=random.nextBoolean();
        boolean oneRoundSkip=random.nextBoolean();
        int golemsPart=0;
        int bossHit=bossDamage;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                if(!(heroesAttackType[i].equals("Golem"))){
                    int partDamage=bossDamage/5;
                    golemsPart+=partDamage;
                    bossHit=bossDamage-bossDamage/5;
                }

                if(heroesAttackType[i].equals("Lucky") && luckyChance){
                    bossHit=0;
                    System.out.println("Lucky had a chance to dodge boss attacks!");
                }
                if(heroesAttackType[i].equals("Berserk")){
                    int berserkPart = (int) (0.2*bossHit);
                    dodgeDamage=bossHit- berserkPart;
                    heroesHealth[i]-= berserkPart;
                }
                if(heroesAttackType[i].equals("Thor") && oneRoundSkip){
                    System.out.println("one round is skipped");
                    bossHit=0;
                }
                if (heroesHealth[i] - bossHit < 0) {
                    heroesHealth[i] = 0;
                } else {
                    heroesHealth[i] = heroesHealth[i] - bossHit;
                }
                if(heroesAttackType[i].equals("Golem")){
                    heroesHealth[i]-=golemsPart;
                }
            }
        }
    }
    public static void docCures(){
        for (int i=0; i<heroesHealth.length; i++){
            if (heroesHealth[i]<100 && heroesHealth[3]>0 && !(heroesAttackType[i].equals("Medic"))){
                heroesHealth[i]=heroesHealth[i]+healthCure;
                break;
            }
            if(heroesHealth[i]<=0 && heroesHealth[3]>0 && !(heroesAttackType[i].equals("Medic"))){
                heroesHealth[i]=heroesHealth[i];
            }
        }
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesAttackType.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0 && !(heroesAttackType[i].equals("Medic"))) {
                int hit = heroesDamage[i];
                if (heroesAttackType[i] == bossDefenceType) {
                    Random random = new Random();
                    int coefficient = random.nextInt(6) + 2; // 2,3,4,5,6,7
                    hit = heroesDamage[i] * coefficient;
                    System.out.println("Critical damage: " + hit);
                }
                if(heroesAttackType[i].equals("Berserk")){
                    hit+=dodgeDamage;
                }
                if (bossHealth - hit < 0) {
                    bossHealth = 0;
                } else {
                    bossHealth = bossHealth - hit;
                }
            }
        }
    }
}

