package logic.uiAdapter;

import DTO.missionResult.MissionResult;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private Consumer<MissionResult> addCandidate;
    private Consumer<Integer> updateTotalCandidates;
    private Consumer<Integer> updateTotalMissionDone;

    public UIAdapter(Consumer<MissionResult> addCandidate,
                     Consumer<Integer> updateTotalCandidates,
                     Consumer<Integer> updateTotalMissionDone) {
        this.addCandidate = addCandidate;
        this.updateTotalCandidates = updateTotalCandidates;
    }

    public void addNewCandidate(MissionResult missionResult) {
        Platform.runLater(
                () -> addCandidate.accept(missionResult)
        );
    }

    public void updateTotalCandidates(int delta) {
        Platform.runLater(
                () -> updateTotalCandidates.accept(delta)
        );
    }

    public void updateTotalMissionDone(int delta){
        Platform.runLater(
                () -> updateTotalMissionDone.accept(delta)
        );
    }
}
