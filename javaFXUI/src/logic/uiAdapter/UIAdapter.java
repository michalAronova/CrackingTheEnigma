package logic.uiAdapter;

import DTO.missionResult.MissionResult;
import javafx.application.Platform;

import java.util.function.Consumer;

public class UIAdapter {

    private final Consumer<MissionResult> addCandidate;
    private final Consumer<Integer> updateTotalMissionDone;

    private final Consumer<Long> updateTotalTime;

    public UIAdapter(Consumer<MissionResult> addCandidate,
                     Consumer<Integer> updateTotalMissionDone,
                     Consumer<Long> updateTotalTime) {
        this.addCandidate = addCandidate;
        this.updateTotalMissionDone = updateTotalMissionDone;
        this.updateTotalTime = updateTotalTime;
    }

    public void addNewCandidate(MissionResult missionResult) {
        Platform.runLater(
                () -> addCandidate.accept(missionResult)
        );
    }

    public void updateTotalMissionDone(int delta, long timeDelta){
        Platform.runLater(
                () -> updateTotalMissionDone.accept(delta)
        );
        Platform.runLater(
                () -> updateTotalTime.accept(timeDelta)
        );
    }
}
