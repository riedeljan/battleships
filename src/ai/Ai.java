package ai;

import java.util.Random;

import gameboard.Board;
import data.Game;

public class Ai {
    // MEMBER VARIABLES
    private static Random randomGenerator = new Random();

    private static Board aiBoard = new Board();
    private static Board aiStrikes = new Board();

    private static int boardWidth;
    private static int boardHeight;

    private static boolean placed;

    private static Trace trace = new Trace();

    // CONSTRUCTOR
    public Ai() {
        boardWidth = data.DataContainer.getGameboardWidth();
        boardHeight = data.DataContainer.getGameboardHeight();
    }

    // PUBLIC METHODS
    public void draw() {
        // do all actions here
        System.out.println("AI draw!");
        data.DataContainer.setAllowed(false);
        if (!placed) {
            place();
            placed = true;
        }
        System.out.println("AI eval!");
        eval();
    }

    public int hit(int x, int y) {
        int field = aiBoard.checkboard(x, y);
        switch (field) {
            case 0:         // Wasser
                return 0;
            case 1:         // Schiff
                return 1;
            case 2:         // Bereits versenktes Schiff
                return 2;
            default:
                return -1;  // To prevent compiler warnings and for the case of errors
        }
    }

    // PRIVATE METHODS
    private void eval() {
        /* evaluate next target, fire and handle results */
        int x = 0, y = 0;


        if (trace.getSize() == 0) {
            do {
                x = randomGenerator.nextInt(boardWidth);
                y = randomGenerator.nextInt(boardHeight);
            } while (aiStrikes.getPlayershots(x, y) != 9);

        } else {
            int traceLen = trace.getSize();
            if (traceLen > 1) {
                int x1 = trace.getTile(0)[0];
                int x2 = trace.getTile(1)[0];
                int y1 = trace.getTile(0)[1];
                int y2 = trace.getTile(1)[1];

                if (y1 == y2) {
                    // orientation horizontal
                    int step;
                    int diff = x1 - x2;
                    if (diff > 0) {
                        // direction left
                        if (diff == 1) {
                            step = 2;
                        } else {
                            step = 1;
                        }

                        if ((x1 - step) >= 0 && aiStrikes.getPlayershots((x1 - step), y1) == 9) {
                            x = x1 - step;
                        } else {
                            while ((x1 - step) >= 0 && aiStrikes.getPlayershots((x1 - step), y1) != 9) {
                                if (aiStrikes.getPlayershots((x1 - step), y1) == 0) {
                                    step = 1;
                                    while (aiStrikes.getPlayershots((x1 + step), y1) != 9) {
                                        step += 1;
                                    }
                                    x = x1 + step;
                                    break;
                                } else {
                                    step += 1;
                                    x = x1 - step;
                                }
                            }

                            if ((x1 - step) < 0) {
                                step = 1;
                                while (aiStrikes.getPlayershots((x1 + step), y1) != 9) {
                                    step += 1;
                                }
                                x = x1 + step;
                            }
                        }

                        y = y1;
                    } else if (diff < 0) {
                        // direction right
                        if (diff == -1) {
                            step = 2;
                        } else {
                            step = 1;
                        }

                        if ((x1 + step) < boardWidth && aiStrikes.getPlayershots((x1 + step), y1) == 9) {
                            x = x1 + step;
                        } else {
                            while ((x1 + step) < boardWidth && aiStrikes.getPlayershots((x1 + step), y1) != 9) {
                                if (aiStrikes.getPlayershots((x1 + step), y1) == 0) {
                                    step = 1;
                                    while (aiStrikes.getPlayershots((x1 - step), y1) != 9) {
                                        step += 1;
                                    }
                                    x = x1 - step;
                                    break;
                                } else {
                                    step += 1;
                                    x = x1 + step;
                                }
                            }

                            if ((x1 + step) >= boardWidth) {
                                step = 1;
                                while (aiStrikes.getPlayershots((x1 - step), y1) != 9) {
                                    step += 1;
                                }
                                x = x1 - step;
                            }
                        }

                        y = y1;
                    }
                } else if (x1 == x2) {
                    //orientation vertical
                    int step;
                    int diff = y1 - y2;
                    if (diff > 0) {
                        // direction left
                        if (diff == 1) {
                            step = 2;
                        } else {
                            step = 1;
                        }

                        if ((y1 - step) >= 0 && aiStrikes.getPlayershots(x1, (y1 - step)) == 9) {
                            y = y1 - step;
                        } else {
                            while ((y1 - step) >= 0 && aiStrikes.getPlayershots(x1, (y1 - step)) != 9) {
                                if (aiStrikes.getPlayershots(x1, (y1 - step)) == 0) {
                                    step = 1;
                                    while (aiStrikes.getPlayershots(x1, (y1 + step)) != 9) {
                                        step += 1;
                                    }
                                    y = y1 + step;
                                    break;
                                } else {
                                    step += 1;
                                    y = y1 - step;
                                }
                            }

                            if ((y1 - step) < 0) {
                                step = 1;
                                while (aiStrikes.getPlayershots(x1, (y1 + step)) != 9) {
                                    step += 1;
                                }
                                y = y1 + step;
                            }
                        }

                        x = x1;
                    } else if (diff < 0) {
                        // direction right
                        if (diff == -1) {
                            step = 2;
                        } else {
                            step = 1;
                        }

                        if ((y1 + step) < boardHeight && aiStrikes.getPlayershots(x1, (y1 + step)) == 9) {
                            y = y1 + step;
                        } else {
                            while ((y1 + step) < boardHeight && aiStrikes.getPlayershots(x1, (y1 + step)) != 9) {
                                if (aiStrikes.getPlayershots(x1, (y1 + step)) == 0) {
                                    step = 1;
                                    while (aiStrikes.getPlayershots(x1, (y1 - step)) != 9) {
                                        step += 1;
                                    }
                                    y = y1 - step;
                                    break;
                                } else {
                                    step += 1;
                                    y = y1 + step;
                                }
                            }

                            if ((y1 + step) >= boardHeight) {
                                step = 1;
                                while (aiStrikes.getPlayershots(x1, (y1 - step)) != 9) {
                                    step += 1;
                                }
                                y = y1 - step;
                            }
                        }

                        x = x1;
                    }
                }
            } else if (traceLen == 1) {
                // try any direction
                int x1 = trace.getTile(0)[0];
                int y1 = trace.getTile(0)[1];
                switch (0) {
                    case 0: // top
                        if ((y1 - 1) >= 0 && aiStrikes.getPlayershots(x1, (y1 - 1)) == 9) {
                            x = x1;
                            y = y1 - 1;
                            break;
                        }
                        // fall-through intended
                    case 1: // right
                        if ((x1 + 1) < boardWidth && aiStrikes.getPlayershots((x1 + 1), y1) == 9) {
                            x = x1 + 1;
                            y = y1;
                            break;
                        }
                        // fall-through intended
                    case 2: // bottom
                        if ((y1 + 1) < boardHeight && aiStrikes.getPlayershots(x1, (y1 + 1)) == 9) {
                            x = x1;
                            y = y1 + 1;
                            break;
                        }
                        // fall-through intended
                    case 3: // left
                        if ((x1 - 1) >= 0 && aiStrikes.getPlayershots((x1 - 1), y1) == 9) {
                            x = x1 - 1;
                            y = y1;
                            break;
                        }
                }

            }
        }

        System.out.println("AI shoot!");
        int ret = fire(x, y);    // 0: Wasser, 1: Treffer, 2: versenkt
        aiStrikes.setPlayershots(x, y, ret);
        switch (ret) {
            case 0:
                System.out.println("Just water here :/");
                break;
            case 1:
                trace.addTile(x, y);
                System.out.println("Hit ship! Size:" + trace.getSize());
                break;
            case 2:
                trace.clear();
                System.out.println("Destroyed ship!");
                break;
            default:
                System.out.println("Unexpected return value: " + ret);
        }
    }

    private int fire(int x, int y) {
        /* fire on other player and handle result */
        return Game.getHit(x, y);
    }

    private void place() {
        /* place ships on Board */
    }
}