package com.huizhuang.zxsq.http.bean.supervision;

import java.util.List;

public class ScoreOptionListList {
    private List<ScoreOptionList> score_list;

    public List<ScoreOptionList> getScore_list() {
        return score_list;
    }

    public void setScore_list(List<ScoreOptionList> score_list) {
        this.score_list = score_list;
    }

    @Override
    public String toString() {
        return "ScoreOptionListList [score_list=" + score_list + "]";
    }

}
