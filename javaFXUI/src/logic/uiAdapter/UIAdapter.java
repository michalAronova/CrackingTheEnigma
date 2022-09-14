package logic.uiAdapter;

import DTO.missionResult.MissionResult;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private Consumer<MissionResult> addCandidate;
    private Consumer<Integer> updateTotalMissionDone;

    public UIAdapter(Consumer<MissionResult> addCandidate,
                     Consumer<Integer> updateTotalMissionDone) {
        this.addCandidate = addCandidate;
        this.updateTotalMissionDone = updateTotalMissionDone;
    }

    public void addNewCandidate(MissionResult missionResult) {
        Platform.runLater(
                () -> addCandidate.accept(missionResult)
        );
    }

    public void updateTotalMissionDone(int delta){
        Platform.runLater(
                () -> updateTotalMissionDone.accept(delta)
        );
    }
}
