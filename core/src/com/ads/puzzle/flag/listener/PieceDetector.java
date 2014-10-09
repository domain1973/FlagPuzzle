package com.ads.puzzle.flag.listener;

import com.ads.puzzle.flag.Answer;
import com.ads.puzzle.flag.actors.Area;
import com.ads.puzzle.flag.actors.Piece;
import com.ads.puzzle.flag.controller.AreaController;
import com.ads.puzzle.flag.controller.IController;
import com.ads.puzzle.flag.controller.PieceController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/7/4.
 */
public class PieceDetector extends GestureDetector {
    private Stage stage;
    private String[][] answers = {{"2,3,6", "0,7,8", "2,5,6", "0,1,8"}, {"1,7", "3,5", "1,7", "3,5"}, {"4,7", "4,5", "1,4", "3,4"}, {"2,7", "0,5", "1,6", "3,8"}};
    private int[][] areaAnswers = {{0, -1, 1, 2, 3, 2, 4, 1, 0}, {3, -1, 1, -1, 2, 0, 1, 4, 3}, {4, 0, 2, 3, -1, 3, 1, -1, 4}, {-1, -1, -1, -1, 4, 1, 3, 0, 2}};

    /**
     * Creates a new GestureDetector with default values: halfTapSquareSize=20, tapCountInterval=0.4f, longPressDuration=1.1f,
     * maxFlingDelay=0.15f.
     *
     * @param listener
     */
    public PieceDetector(Stage stage, GestureListener listener) {
        super(listener);
        this.stage = stage;
    }

    public boolean isPass(int gateNum) {
        AreaController areaCtrl = (AreaController) stage.getRoot().findActor(IController.AREA_CTRL);
        SnapshotArray<Actor> children = areaCtrl.getChildren();
        for (Actor actor : children) {
            Area area = (Area) actor;
            if (area.getPieceId() == -1) {
                return false;
            }
        }

        PieceController pieceController = (PieceController) stage.getRoot().findActor(IController.PIECE_CTRL);
        SnapshotArray<Actor> actors = pieceController.getChildren();
        Integer[] temps = Answer.CHALLENGES[gateNum];
        List<Integer> challengeIDs = new ArrayList<Integer>();
        for (Integer temp : temps) {
            challengeIDs.add(temp);
        }
        for (Actor actor : actors) {
            Piece piece = (Piece) actor;
            if (piece.getArea() > -1) {
                int areaId = piece.getArea();
                int pieceId = piece.getId();
                int orientation = piece.getOrientation();
                String[] a = answers[pieceId][orientation].split(",");
                for (String str : a) {
                    int c = areaAnswers[areaId][Integer.parseInt(str)];
                    if (c > -1) {
                        int index = challengeIDs.indexOf(c);
                        if (index == -1) {
                            return false;
                        } else {
                            challengeIDs.remove(index);
                        }
                    }
                }
            }
        }
        return challengeIDs.size() == 0;
    }
}
