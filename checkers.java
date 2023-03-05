import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public
    class checkers {
    private static long getBt(long value, long position) {
        return (value >> position) & 1;
    }

    long board = 0L;
    int board1 = 00011010;
    //int board1 = 00011010;

    private static long getBts(long value, long startPosition, long count) {
        return (value >> startPosition) & ~(~0L << count);
    }

    private static long resetBt(long value, long position) {
        return value & ~(1L << position);
    }

    private static long resetBts(long value, long startPosition, long count) {
        return value & ~(~(~0L << count) << startPosition);
    }

    private static long copyBt(long value, long position, long bit) {
        return resetBt(value, position) | (bit << position);
    }

    private static long copyBts(long value, long startPosition, long count, long bits) {
        return resetBts(value, startPosition, count) | (bits << startPosition);
    }



    private static long getPawn(long pawns, long index) {
        return getBts(pawns, 9 * index, 9);
    }

    private static long getPawn(long pawns1, long pawns2, long index) {
        return index < 6 ? getPawn(pawns1, index) : getPawn(pawns2, index - 6);
    }

    private static long getPawnPositionX(long pawn) {
        return getBts(pawn, 0, 3);
    }

    private static long getPawnPositionY(long pawn) {
        return getBts(pawn, 3, 3);
    }

    private static boolean getPawnIsWhite(long pawn) {
        return getBt(pawn, 6) == 1;
    }

    private static boolean getPawnIsQueen(long pawn) {
        return getBt(pawn, 7) == 1;
    }

    private static boolean getPawnIsCaptured(long pawn) {
        return getBt(pawn, 8) == 1;
    }



    private static long setPawn(long pawns, long index, long pawn) {
        return copyBts(pawns, 9L * index, 9, pawn);
    }

    private static long setPawnPositionX(long pawn, long positionX) {
        return copyBts(pawn, 0, 3, positionX);
    }

    private static long setPawnPositionY(long pawn, long positionY) {
        return copyBts(pawn, 3, 3, positionY);
    }

    private static long setPawnPosition(long pawn, long positionX, long positionY) {
        pawn = setPawnPositionX(pawn, positionX);
        pawn = setPawnPositionY(pawn, positionY);
        return pawn;
    }

    private static long setPawnIsWhite(long pawn, boolean isWhite) {
        return copyBt(pawn, 6, isWhite ? 1 : 0);
    }

    private static long setPawnIsQueen(long pawn) {
        return copyBt(pawn, 7, 1);
    }

    private static long setPawnIsCaptured(long pawn) {
        return copyBt(pawn, 8, 1);
    }

    private static long createPawn(long positionX, long positionY, boolean isWhite) {
        long pawn = 0;
        pawn = setPawnPosition(pawn, positionX, positionY);
        pawn = setPawnIsWhite(pawn, isWhite);
        return pawn;
    }



    private static long createBlackPawnsRA() {
        long pawns = 0;
        for (int n = 0; n <= 6; n += 2) {
            pawns <<= 9;
            pawns |= createPawn(n, 7, false);
        }
        for (int n = 1; n <= 3; n += 2) {
            pawns <<= 9;
            pawns |= createPawn(n, 6, false);
        }
        return pawns;
    }

    private static long createWhitePawnsRA() {
        long pawns = 0;
        for (int n = 1; n <= 7; n += 2) {
            pawns <<= 9;
            pawns |= createPawn(n, 0, true);
        }
        for (int n = 0; n <= 2; n += 2) {
            pawns <<= 9;
            pawns |= createPawn(n, 1, true);
        }
        return pawns;
    }

    private static long createBlackPawnsRB() {
        long pawns = 0;
        for (int n = 5; n <= 7; n += 2) {
            pawns <<= 9;
            pawns |= createPawn(n, 6, false);
        }
        for (int n = 0; n <= 6; n += 2) {
            pawns <<= 9;
            pawns |= createPawn(n, 5, false);
        }
        return pawns;
    }

    private static long createWhitePawnsRB() {
        long pawns = 0;
        for (int n = 4; n <= 6; n += 2) {
            pawns <<= 9;
            pawns |= createPawn(n, 1, true);
        }
        for (int n = 1; n <= 7; n += 2) {
            pawns <<= 9;
            pawns |= createPawn(n, 2, true);
        }
        return pawns;
    }

    private static boolean isPawnAtPosition(long positionX, long positionY, long pawn) {
        return getPawnPositionX(pawn) == positionX && getPawnPositionY(pawn) == positionY;
    }

    private static long findPawn(long positionX, long positionY, long pawns) {
        for (int n = 0; n < 6; ++n) {
            long pawn = getPawn(pawns, n);
            if (isPawnAtPosition(positionX, positionY, pawn)) {
                return pawn;
            }
        }
        return -1;
    }

    private static long findPawn(long positionX, long positionY, long pawns1, long pawns2) {
        long pawn = findPawn(positionX, positionY, pawns1);
        return pawn != -1 ? pawn : findPawn(positionX, positionY, pawns2);
    }

    private static long findPawn(long positionX, long positionY, long pawns1, long pawns2, long pawns3, long pawns4) {
        long pawn = findPawn(positionX, positionY, pawns1, pawns2);
        return pawn != -1 ? pawn : findPawn(positionX, positionY, pawns3, pawns4);
    }

    private static long findPawnIndex(long positionX, long positionY, long pawns) {
        for (int n = 0; n < 6; ++n) {
            long pawn = getPawn(pawns, n);
            if (isPawnAtPosition(positionX, positionY, pawn)) {
                return n;
            }
        }
        return -1;
    }

    private static long findPawnIndex(long positionX, long positionY, long pawns1, long pawns2) {
        long index = findPawnIndex(positionX, positionY, pawns1);
        if (index != -1) {
            return index;
        }
        index = findPawnIndex(positionX, positionY, pawns2);
        if (index != -1) {
            return index + 6;
        }
        return index;
    }

    private static boolean allPawnsCaptured(long pawns) {
        for (int n = 0; n < 6; ++n) {
            long pawn = getPawn(pawns, n);
            if (!getPawnIsCaptured(pawn)) {
                return false;
            }
        }
        return true;
    }

    private static boolean allPawnsCaptured(long pawns1, long pawns2) {
        return allPawnsCaptured(pawns1) && allPawnsCaptured(pawns2);
    }

    private static long captureEnemyPawn(long startX, long startY, long endX, long endY, long enemyPawns1, long enemyPawns2) {
        long dx = endX > startX ? 1 : -1;
        long dy = endY > startY ? 1 : -1;

        long pawn;

        for (long posX = startX, posY = startY; posX != endX && posY != endY; posX += dx, posY += dy) {
            pawn = findPawnIndex(posX, posY, enemyPawns1, enemyPawns2);
            if (pawn != -1) {
                return pawn;
            }
        }

        return -1;
    }

    private static boolean isFieldWhite(long positionX, long positionY) {
        return (positionY % 2 == 0) == (positionX % 2 == 0);
    }

    private static void printField(PrintWriter out, long positionX, long positionY, long pawns1, long pawns2, long pawns3, long pawns4) {
        long pawn = findPawn(positionX, positionY, pawns1, pawns2, pawns3, pawns4);

        if (pawn == -1 || getPawnIsCaptured(pawn)) {
            if (isFieldWhite(positionX, positionY)) {
                out.print('\u2B1B');
                return;
            }
            out.print('\u2B1C');
            return;
        }

        if (getPawnIsWhite(pawn)) {
            out.print('\u265F');
            return;
        }

        if (getPawnIsQueen(pawn)) {
            if (getPawnIsWhite(pawn)) {
                out.print('\u2655');
                return;
            }
            out.print('\u265B');
            return;
        }
        out.print('\u2659');
    }

    private static void printBoard(PrintWriter out, long pawns1, long pawns2, long pawns3, long pawns4) {
        for (int m = 0; m < 8; ++m) {
            out.print((8 - m) + " ");
            for (int n = 0; n < 8; ++n) {
                printField(out, n, m, pawns1, pawns2, pawns3, pawns4);
            }
            out.println();
        }
        for (int n = 0; n < 8; ++n) {
            out.print(" " + (char)('a' + n));
        }
        out.println();
    }

    private static long parsePositionX(String string) {
        return Character.toUpperCase(string.charAt(0)) - 'A';
    }

    private static long parsePositionY(String string) {
        return 7 - (string.charAt(1) - '1');
    }


    public static void main(String[] args) {
        long player1PawnsCA = createWhitePawnsRA();
        long player2PawnsCA = createBlackPawnsRA();
        long player1PawnsCB = createWhitePawnsRB();
        long player2PawnsCB = createBlackPawnsRB();

        Scanner sc = new Scanner(System.in);
        PrintWriter out = new PrintWriter(System.out, true, StandardCharsets.UTF_8);

        while (true) {
            printBoard(out, player1PawnsCA, player2PawnsCA, player1PawnsCB, player2PawnsCB);

            {
                out.println("Ruch gracza1 - aby ruszyć pionem, najpierw wybierz jego pozycję");

                long startX;
                long startY;
                long playerPawnIndex;
                long playerPawn;

                while (true) {
                    String start = sc.next();
                    startX = parsePositionX(start);
                    startY = parsePositionY(start);
                    playerPawnIndex = findPawnIndex(startX, startY, player1PawnsCA, player1PawnsCB);
                    playerPawn = getPawn(player1PawnsCA, player1PawnsCB, playerPawnIndex);

                    if (playerPawnIndex == -1 || getPawnIsCaptured(playerPawn)) {
                        out.println("Wybrano nieprawidłową pozycję, wprowadź położenie jeszcze raz");
                        continue;
                    }
                    break;
                }
                out.println("Wprowadź miejsce docelowe piona");

                long endX;
                long endY;
                long enemyPawnIndex;

                while (true) {
                    String end = sc.next();
                    endX = parsePositionX(end);
                    endY = parsePositionY(end);
                    long collideIndex = findPawnIndex(endX, endY, player1PawnsCA, player1PawnsCB);

                    if (collideIndex != -1) {
                        out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                        continue;
                    }
                    enemyPawnIndex = captureEnemyPawn(startX, startY, endX, endY, player2PawnsCA, player2PawnsCB);

                    if (getPawnIsQueen(playerPawn)) {
                        if (Math.abs(endX - startX) != Math.abs(endY - startY)) {
                            out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                            continue;
                        }
                    } else {
                        if (enemyPawnIndex == -1) {
                            if (Math.abs(endX - startX) != 1 || endY - startY != 1) {
                                out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                                continue;
                            }
                        } else {
                            if (Math.abs(endX - startX) != 2 || Math.abs(endY - startY) != 2) {
                                out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                                continue;
                            }
                        }
                    }
                    break;
                }

                playerPawn = setPawnPosition(playerPawn, endX, endY);

                if (endY == 7) {
                    playerPawn = setPawnIsQueen(playerPawn);
                }

                if (playerPawnIndex < 6) {
                    player1PawnsCA = setPawn(player1PawnsCA, playerPawnIndex, playerPawn);
                } else {
                    player1PawnsCB = setPawn(player1PawnsCB, playerPawnIndex - 6, playerPawn);
                }

                if (enemyPawnIndex != -1) {
                    long enemyPawn = getPawn(player2PawnsCA, player2PawnsCA, enemyPawnIndex);
                    enemyPawn = setPawnIsCaptured(enemyPawn);

                    if (enemyPawnIndex < 6) {
                        player2PawnsCA = setPawn(player2PawnsCA, enemyPawnIndex, enemyPawn);
                    } else {
                        player2PawnsCB = setPawn(player2PawnsCB, enemyPawnIndex - 6, enemyPawn);
                    }

                    startX = endX;
                    startY = endY;

                    while (true) {
                        printBoard(out, player1PawnsCA, player1PawnsCB, player2PawnsCA, player2PawnsCB);
                        out.println("Wybierz następne bicie lub wprowadź \"zk\", aby zakończyć");

                        boolean done = false;

                        while (true) {
                            String end = sc.next();

                            if (end.equals("zk")) {
                                done = true;
                                break;
                            }
                            endX = parsePositionX(end);
                            endY = parsePositionY(end);
                            long collideIndex = findPawnIndex(endX, endY, player2PawnsCA, player2PawnsCB);

                            if (collideIndex != -1) {
                                out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                                continue;
                            }

                            enemyPawnIndex = captureEnemyPawn(startX, startY, endX, endY, player1PawnsCA, player1PawnsCB);

                            if (enemyPawnIndex == -1) {
                                out.println("Zbij pion przeciwnika lub zakończ ruch wprowadzając \"zk\"");
                                continue;
                            }

                            if (getPawnIsQueen(playerPawn)) {
                                if (Math.abs(endX - startX) != Math.abs(endY - startY)) {
                                    out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                                    continue;
                                }
                            } else {
                                if (Math.abs(endX - startX) != 2 || Math.abs(endY - startY) != 2) {
                                    out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                                    continue;
                                }
                            }
                            break;
                        }

                        if (done) {
                            break;
                        }

                        playerPawn = setPawnPosition(playerPawn, endX, endY);

                        if (endY == 7) {
                            playerPawn = setPawnIsQueen(playerPawn);
                        }

                        if (playerPawnIndex < 6) {
                            player1PawnsCA = setPawn(player1PawnsCA, playerPawnIndex, playerPawn);
                        } else {
                            player1PawnsCB = setPawn(player1PawnsCB, playerPawnIndex - 6, playerPawn);
                        }

                        enemyPawn = getPawn(player2PawnsCA, player2PawnsCB, enemyPawnIndex);
                        enemyPawn = setPawnIsCaptured(enemyPawn);

                        if (enemyPawnIndex < 6) {
                            player2PawnsCA = setPawn(player2PawnsCA, enemyPawnIndex, enemyPawn);
                        } else {
                            player2PawnsCB = setPawn(player2PawnsCB, enemyPawnIndex - 6, enemyPawn);
                        }

                        startX = endX;
                        startY = endY;
                    }
                }
            }

            if (allPawnsCaptured(player2PawnsCA, player2PawnsCB)) {
                break;
            }

            printBoard(out, player1PawnsCA, player1PawnsCB, player2PawnsCA, player2PawnsCB);

            {
                out.println("Ruch gracza2 - aby ruszyć pionem, najpierw wybierz jego pozycję");

                long startX;
                long startY;
                long playerPawnIndex;
                long playerPawn;

                while (true) {
                    String start = sc.next();
                    startX = parsePositionX(start);
                    startY = parsePositionY(start);
                    playerPawnIndex = findPawnIndex(startX, startY, player2PawnsCA, player2PawnsCB);
                    playerPawn = getPawn(player2PawnsCA, player2PawnsCB, playerPawnIndex);

                    if (playerPawnIndex == -1 || getPawnIsCaptured(playerPawn)) {
                        out.println("Wybrano nieprawidłową pozycję, wprowadź położenie jeszcze raz");
                        continue;
                    }
                    break;
                }

                out.println("Wprowadź miejsce docelowe piona");
                long endX;
                long endY;
                long enemyPawnIndex;

                while (true) {
                    String end = sc.next();
                    endX = parsePositionX(end);
                    endY = parsePositionY(end);
                    long collideIndex = findPawnIndex(endX, endY, player2PawnsCA, player2PawnsCB);

                    if (collideIndex != -1) {
                        out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                        continue;
                    }

                    enemyPawnIndex = captureEnemyPawn(startX, startY, endX, endY, player1PawnsCA, player1PawnsCB);

                    if (getPawnIsQueen(playerPawn)) {
                        if (Math.abs(endX - startX) != Math.abs(endY - startY)) {
                            out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                            continue;
                        }
                    } else {
                        if (enemyPawnIndex == -1) {
                            if (Math.abs(endX - startX) != 1 || endY - startY != -1) {
                                out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                                continue;
                            }
                        } else {
                            if (Math.abs(endX - startX) != 2 || Math.abs(endY - startY) != 2) {
                                out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                                continue;
                            }
                        }
                    }
                    break;
                }

                playerPawn = setPawnPosition(playerPawn, endX, endY);

                if (endY == 0) {
                    playerPawn = setPawnIsQueen(playerPawn);
                }

                if (playerPawnIndex < 6) {
                    player2PawnsCA = setPawn(player2PawnsCA, playerPawnIndex, playerPawn);
                } else {
                    player2PawnsCB = setPawn(player2PawnsCB, playerPawnIndex - 6, playerPawn);
                }

                if (enemyPawnIndex != -1) {
                    long enemyPawn = getPawn(player1PawnsCA, player1PawnsCB, enemyPawnIndex);
                    enemyPawn = setPawnIsCaptured(enemyPawn);

                    if (enemyPawnIndex < 6) {
                        player1PawnsCA = setPawn(player1PawnsCA, enemyPawnIndex, enemyPawn);
                    } else {
                        player1PawnsCB = setPawn(player1PawnsCB, enemyPawnIndex - 6, enemyPawn);
                    }

                    startX = endX;
                    startY = endY;

                    while (true) {
                        printBoard(out, player1PawnsCA, player1PawnsCB, player2PawnsCA, player2PawnsCB);
                        out.println("Wybierz następne bicie lub wprowadź \"zk\", aby zakończyć");

                        boolean done = false;

                        while (true) {
                            String end = sc.next();
                            if (end.equals("zk")) {
                                done = true;
                                break;
                            }

                            endX = parsePositionX(end);
                            endY = parsePositionY(end);
                            long collideIndex = findPawnIndex(endX, endY, player2PawnsCA, player2PawnsCB);

                            if (collideIndex != -1) {
                                out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                                continue;
                            }

                            enemyPawnIndex = captureEnemyPawn(startX, startY, endX, endY, player1PawnsCA, player1PawnsCB);

                            if (enemyPawnIndex == -1) {
                                out.println("Zbij pion przeciwnika lub zakończ ruch wprowadzając \"zk\"");
                                continue;
                            }

                            if (getPawnIsQueen(playerPawn)) {
                                if (Math.abs(endX - startX) != Math.abs(endY - startY)) {
                                    out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                                    continue;
                                }
                            } else {
                                if (Math.abs(endX - startX) != 2 || Math.abs(endY - startY) != 2) {
                                    out.println("Wpisano nieprawidłową pozycję, wprowadź ruch jeszcze raz");
                                    continue;
                                }
                            }
                            break;
                        }

                        if (done) {
                            break;
                        }

                        playerPawn = setPawnPosition(playerPawn, endX, endY);

                        if (endY == 0) {
                            playerPawn = setPawnIsQueen(playerPawn);
                        }

                        if (playerPawnIndex < 6) {
                            player2PawnsCA = setPawn(player2PawnsCA, playerPawnIndex, playerPawn);
                        } else {
                            player2PawnsCB = setPawn(player2PawnsCB, playerPawnIndex - 6, playerPawn);
                        }

                        enemyPawn = getPawn(player1PawnsCA, player1PawnsCB, enemyPawnIndex);
                        enemyPawn = setPawnIsCaptured(enemyPawn);

                        if (enemyPawnIndex < 6) {
                            player1PawnsCA = setPawn(player1PawnsCA, enemyPawnIndex, enemyPawn);
                        } else {
                            player1PawnsCB = setPawn(player1PawnsCB, enemyPawnIndex - 6, enemyPawn);
                        }

                        startX = endX;
                        startY = endY;
                    }
                }
            }

            if (allPawnsCaptured(player1PawnsCA, player1PawnsCB)) {
                break;
            }
        }

        if (allPawnsCaptured(player2PawnsCA, player2PawnsCB)) {
            out.println("Wygrywa gracz1");
        } else {
            out.println("Wygrywa gracz2");
        }
    }
}
