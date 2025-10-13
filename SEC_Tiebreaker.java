import java.util.Scanner;
import java.util.ArrayList;
// import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Random;

public class SEC_Tiebreaker { 
    public static void main(String[] args) {
        // Adds teams to the list.
        addTeams();

        // Create an input.
        Scanner input = new Scanner(System.in);

        //Introduce the tiebreaker.
        System.out.println("This program is designed to break ties between teams in the SEC.");

        // Input the amount of tied teams.
        int number = -1;
        do {
            try {
                System.out.print("Enter the number of teams tied: ");
                number = input.nextInt();
            } catch (InputMismatchException misinput) {
                System.out.println("You did not input a whole number. Please input a whole number.");
                input.nextLine();
            }
        } while (number < 0);
        if (number == 0 || number == 1) {
            input.close();
            System.out.println("No teams are tied with each other. Please start over when two or more teams are tied.");
            return;
        } else if (number > teams.size()) {
            input.close();
            System.out.printf("There are only %d teams in the SEC. Please start over with %d teams or less as tied.\n", teams.size(), teams.size());
            return;
        }

        // Input the tied teams.
        int idx = 0;
        input.nextLine();
        while (idx < number) {
            System.out.printf("Enter team %d: ", idx + 1);
            String team = input.nextLine();
            if (teams.contains(team) && !(teams_tied.contains(team))) {
                teams_tied.add(team);
                idx++;
            } else {
                System.out.println("Invalid team name. Try again.");
            }
        }
        System.out.println(teams_tied.toString());
        setSize();
        old_len_teams_tied = len_teams_tied;

        // Run the tiebreaker methods.
        H2H();

        // Close the input.
        input.close();
    }

    // Creating list of teams.
    private static ArrayList<String> teams = new ArrayList<>();

    // Creating list of tied teams.

    private static ArrayList<String> teams_tied = new ArrayList<>();
    private static int len_teams_tied = teams_tied.size();
    private static ArrayList<String> old_teams_tied = new ArrayList<>();
    private static int old_len_teams_tied = len_teams_tied;

    // Method to add teams to list.
    public static void addTeams() {
        teams.add("Alabama");
        teams.add("Arkansas");
        teams.add("Auburn");
        teams.add("Florida");
        teams.add("Georgia");
        teams.add("Kentucky");
        teams.add("Louisiana State");
        teams.add("Mississippi");
        teams.add("Mississippi State");
        teams.add("Missouri");
        teams.add("Oklahoma");
        teams.add("South Carolina");
        teams.add("Tennessee");
        teams.add("Texas");
        teams.add("Texas A&M");
        teams.add("Vanderbilt");
    }

    // Tiebreaker 1: Head-to-Head
    private static void H2H() {
        int idx = 0;
        Scanner input = new Scanner(System.in);
        System.out.println("Test 1: Head-to-Head");
        System.out.print("Did all the tied teams play each other? ");
        String response_1 = input.next();
        if (response_1.equalsIgnoreCase("yes")) {
            System.out.println("Please state how many wins each team got over the other tied teams.");
            ArrayList<Integer> teams_tied_league = new ArrayList<>();
            for (int i = 0; i < len_teams_tied; i++) {
                String team = teams_tied.get(i);
                int wins = -1;
                do {
                    try {
                        System.out.printf("How many wins did %s have over the other tied teams? ", team);
                        wins = input.nextInt();
                    } catch (InputMismatchException misinput) {
                        System.out.println("You did not input a whole number. Please input a whole number.");
                        input.nextLine();
                    }
                } while (wins < 0);
                teams_tied_league.add(wins);
            }
            // System.out.println(teams_tied);
            // System.out.println(teams_tied_league);
            int min_wins = -1;
            for (int j = 0; j < len_teams_tied; j++) {
                if (teams_tied_league.get(j) > min_wins) {
                    min_wins = teams_tied_league.get(j);
                }
            }
            if (min_wins <= -1) {
                System.out.println("This result is impossible. Please restart the program with the same teams.");
                input.close();
                return;
            } else {
                while (idx < len_teams_tied) {
                    if (teams_tied_league.get(idx) < min_wins) {
                        teams_tied.remove(idx);
                        teams_tied_league.remove(idx);
                        len_teams_tied--;
                    } else {
                        idx++;
                    }
                }
            }
        } else if (len_teams_tied >= 3) {
            System.out.println("Test 1.5: Winner Against All Others Wins, Loser to All Others Eliminated");
            ArrayList<String> responses = new ArrayList<>();
            int k = 0;
            while (k < len_teams_tied) {
                System.out.printf("Did %s beat ALL the other tied teams? ", teams_tied.get(k));
                String response_2 = input.next();
                if (response_2.equalsIgnoreCase("yes")) {
                    System.out.printf("%s wins the tiebreaker!", teams_tied.get(k));
                    input.close();
                    return;
                } else if (response_2.equalsIgnoreCase("no")) {
                    System.out.printf("Did %s lose to ALL the other tied teams? ", teams_tied.get(k));
                    String response_3 = input.next();
                    if (response_3.equalsIgnoreCase("yes") || response_3.equalsIgnoreCase("no")) {
                        responses.add(response_3);
                        k++;
                    } else {
                        System.out.println("Invalid response. Only enter 'yes' or 'no'.");
                    }
                } else {
                    System.out.println("Invalid response. Only enter 'yes' or 'no'.");
                }
            }
            // System.out.println(responses.toString());
            int i = 0;
            while (i < len_teams_tied) {
                // System.out.println(responses.toString());
                if (responses.get(i).equalsIgnoreCase("yes")) {
                    System.out.printf("%s was removed from the tiebreaker.\n", teams_tied.get(i));
                    teams_tied.remove(i);
                    responses.remove(i);
                    len_teams_tied--;
                } else {
                    i++;
                }
            }
        }
        if (!check()) {
            common_opponents();
        }
        input.close();
    }

    // Tiebreaker 2: Win Percentage against Common Conference Opponents
    private static void common_opponents() {
        Scanner input = new Scanner(System.in);
        System.out.println("Test 2: Win Percentage Against Common Opponents");
        // System.out.println("This part of the tiebreaker is under development. Sorry.");
        System.out.print("Did the currently tied teams play any common opponents? ");
        String saved_response = "";
        while (true) {
            String response_4 = input.next();
            if (response_4.equalsIgnoreCase("yes") || response_4.equalsIgnoreCase("no")) {
                saved_response = response_4;
                break;
            } else {
                System.out.println("Please only answer 'yes' or 'no'. Did the tied teams play any common opponents? ");
            }
        }
        // boolean marker = false;
        if (saved_response.equalsIgnoreCase("yes")) {
            int com_num = -1;
            do {
                try {
                    System.out.printf("How many common opponents did the tied teams play? ");
                        com_num = input.nextInt();
                } catch (InputMismatchException misinput) {
                    System.out.println("You did not input a whole number. Please input a whole number.");
                    input.nextLine();
                }
            } while (com_num < 0);
            int idx2 = 0;
            ArrayList<String> com_opps = new ArrayList<>();
            input.nextLine();
            while (idx2 < com_num) {
                System.out.printf("Enter common opponent %d: ", idx2 + 1);
                String opp = input.nextLine();
                if (teams.contains(opp) && !(teams_tied.contains(opp)) && !(com_opps.contains(opp))) {
                    com_opps.add(opp);
                    idx2++;
                } else {
                    System.out.println(" Invalid team name. Try again.");
                }
            }
            System.out.println(com_opps);
            // System.out.println("This part is still in development. Sorry.");
            ArrayList<Integer> com_wins = new ArrayList<>();
            for (int i = 0; i < len_teams_tied; i++) {
                String team = teams_tied.get(i);
                int wins = -1;
                do {
                    try {
                        System.out.printf("How many wins did %s have over the common opponents? ", team);
                        wins = input.nextInt();
                    } catch (InputMismatchException misinput) {
                        System.out.println("You did not input a whole number. Please input a whole number.");
                        input.nextLine();
                    }
                } while (wins < 0);
                com_wins.add(wins);
            }
            System.out.println(com_wins);
            System.out.println(teams_tied);
            // System.out.println("This part is still under development. Sorry.");
            // need method that checks max element in ArrayList and saved count of them
            int max_wins = -1;
            int counter = 0;
            for (int i = 0; i < com_wins.size(); i++) {
                int team_wins = com_wins.get(i);
                if (team_wins > max_wins) {
                    max_wins = team_wins;
                    counter = 1;
                } else if (team_wins == max_wins) {
                    counter += 1;
                }
            }
            System.out.printf("Highest win total: %d, Number of teams with highest win total: %d \n", max_wins, counter);
            // System.out.println("This part is still under development. Sorry.");
            int idx3 = 0;
            while (idx3 < len_teams_tied) {
                if (com_wins.get(idx3) != max_wins) {
                    com_wins.remove(idx3);
                    teams_tied.remove(idx3);
                    len_teams_tied--;
                } else {
                    idx3++;
                }
            }
            if (!check()) {
                rank_com_opps(com_opps, com_num, com_wins);
            }
        } else {
            if (!check()) {
                SoR();
            }
        }
        input.close();
    }

    // Tiebreaker 3: Rank Common Conference Opponents
    private static void rank_com_opps(ArrayList<String> com_opps, int com_num, ArrayList<Integer> com_wins) {
        int m = 0;
        while (old_teams_tied.size() < len_teams_tied) {
            old_teams_tied.add(teams_tied.get(m));
            m++;
        }
        Scanner input = new Scanner(System.in);
        System.out.println("Step 3: Rank of Common Conference Opponents");
        System.out.println("Assign a rank to each common opponent based on conference standings.");
        System.out.println("If you need to break any ties, restart the program with the common opponents to do so.");
        int z = 0;
        ArrayList<Integer> ranks = new ArrayList<>();
        while (z < com_num) {
            int rank = -1;
            do {
                try {
                    System.out.printf("Assign a rank of 1 to %d for %s: ", com_num, com_opps.get(z));
                    rank = input.nextInt();
                } catch (InputMismatchException misinput) {
                    System.out.println("You did not input a whole number. Please input a whole number.");
                    input.nextLine();
                }
            } while (rank < 0);
                if (ranks.contains(rank) || rank < 1 || rank > com_num) {
                    System.out.println("Invalid rank. Try again.");
                } else {
                    ranks.add(rank);
                    z++;
                }
        }
        System.out.println(com_opps);
        System.out.println(ranks);
        ArrayList<ArrayList<Integer>> ranked_com_opps = new ArrayList<>();
        for (int i = 0; i < len_teams_tied; i++) {
            String team_selected = teams_tied.get(i);
            int highest_rank = 1;
            ArrayList <Integer> ranked_com_opp = new ArrayList<>();
            while (highest_rank <= com_num) {
                int idx_r = 0;
                int idx_c = 0;
                while (idx_r < com_num) {
                    if (ranks.get(idx_r) == highest_rank) {
                        idx_c = idx_r;
                        break;
                    }
                    idx_r++;
                }
                System.out.printf("Did %s defeat %s? ",team_selected, com_opps.get(idx_c));
                String response_3 = input.next();
                if (response_3.equalsIgnoreCase("yes")) {
                    ranked_com_opp.add(1);
                } else if (response_3.equalsIgnoreCase("no")) {
                    ranked_com_opp.add(0);
                } else {
                    System.out.println("Invalid response. Only enter 'yes' or 'no'.");
                }
                highest_rank++;
            }
            ranked_com_opps.add(ranked_com_opp);
            System.out.printf("%s's results are confirmed.\n", team_selected);
        }
        System.out.println(ranked_com_opps);
        int idx_rco = 0;
        while ((len_teams_tied > 1) && (idx_rco < com_num)) {
            ArrayList<Integer> comparison = new ArrayList<>();
            for (int i = 0; i < len_teams_tied; i++) {
                comparison.add(ranked_com_opps.get(i).get(idx_rco));
            }
            System.out.println(comparison);
            int idx_comp = 0;
            while (idx_comp < len_teams_tied) {
                if (comparison.get(idx_comp) == 0) {
                    com_wins.remove(idx_comp);
                    comparison.remove(idx_comp);
                    teams_tied.remove(idx_comp);
                    len_teams_tied--;
                    // System.out.println(teams_tied);
                } else {
                    idx_comp++;
                }
            } if (check()) {
                input.close();
                return;
            }
            idx_rco++;
            System.out.println("This rank is did not eliminate anyone.");
        }
        if (!check()) {
            SoR();
        }
        input.close();
    }

    // Tiebreaker 4: Combined Win Percentage of Conference Opponents (strength of record)
    private static void SoR() {
        Scanner input = new Scanner(System.in);
        System.out.println("Step 4: Conference Strength-of-Record");
        ArrayList<Double> records = new ArrayList<>();
        for (int i = 0; i < len_teams_tied; i++) {
            String team = teams_tied.get(i);
            int comb_win = -1;
            do {
                try {
                    System.out.printf("What is the COMBINED win total of %s's conference opponents? ", team);
                    comb_win = input.nextInt();
                } catch (InputMismatchException misinput) {
                    System.out.println("You did not input a whole number. Please input a whole number.");
                    input.nextLine();
                }
            } while (comb_win < 0);
            int comb_loss = -1;
            do {
                try {
                    System.out.printf("What is the COMBINED loss total of %s's conference opponents? ", team);
                    comb_loss = input.nextInt();
                } catch (InputMismatchException misinput) {
                    System.out.println("You did not input a whole number. Please input a whole number.");
                    input.nextLine();
                }
            } while (comb_loss < 0);
            double record = (double) comb_win / (double) (comb_win + comb_loss);
            System.out.println(record);
            records.add(record);
        }
        System.out.println(records);
        double best_record = 0.0;
        int count = 0;
        for (int i = 0; i < len_teams_tied; i++) {
            double team_record = records.get(i);
            if (team_record > best_record) {
                best_record = team_record;
                count = 1;
            } else if (team_record == best_record) {
                count++;
            }
        }
        System.out.println(count);
        System.out.println(best_record);
        int idx4 = 0;
        // int new_len = count;
        ArrayList<String> teams_still_tied = new ArrayList<>();
        while (count > 0) {
            // System.out.println("Still alive");
            if (records.get(idx4) == best_record) {
                teams_still_tied.add(teams_tied.get(idx4));
                count--;
            }
            idx4++;
        }
        System.out.println(teams_still_tied);
        teams_tied = teams_still_tied;
        setSize();
        if (!check()) {
            SportSource();
        }
        input.close();
    }

    // Tiebreaker 5: SportSource Analytics Rankings
    private static void SportSource() {
        System.out.println("Step 5: SportSource Rankings");
        System.out.println("This step is impossible to get without paying SportSource, so it will be skipped.");
        Random_Selection();
        Scanner input = new Scanner (System.in);
        if (!check()) {
            Random_Selection();
        }
        input.close();
    }

    // Tiebreaker 6: Random Selection
    private static void Random_Selection() {
        System.out.println("Step 6: Random Draw");
        Random rand = new Random();
        int final_idx = rand.nextInt(len_teams_tied);
        // System.out.println(final_idx);
        System.out.printf("%s wins the tiebreaker!\n", teams_tied.get(final_idx));
    }

    // Set the size of the tied teams list
    private static void setSize() {
        len_teams_tied = teams_tied.size();
    }

    // Check to see if there is only one person left or if there needs to be a reset
    private static boolean check() {
        if (len_teams_tied == 1) {
            System.out.printf("%s wins the tiebreaker!", teams_tied.get(0));
            return true;
        } else if (len_teams_tied == 0) {
            // System.out.println("Restore required");
            int restore = 0;
            while (old_teams_tied.size() > len_teams_tied) {
                teams_tied.add(old_teams_tied.get(restore));
                restore++;
                len_teams_tied++;
                // System.out.println(teams_tied);
            }
        } else if (len_teams_tied != old_len_teams_tied) {
            System.out.printf("The tiebreaker will be restarted with the remaining %d tied teams.\n", len_teams_tied);
            System.out.println(teams_tied);
            Scanner input = new Scanner(System.in);
            old_len_teams_tied = len_teams_tied;
            H2H();
            input.close();
            return true;
        }
        System.out.println("The teams are still tied.");
        return false;
    }
}
