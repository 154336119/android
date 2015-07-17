package com.huizhuang.zxsq.http.bean.supervision;

import java.util.List;


public class ScoreOptionList {
    private float score;
    private List<ScoreOption> list;

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public List<ScoreOption> getList() {
        return list;
    }

    public void setList(List<ScoreOption> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "ScoreOptionList [score=" + score + ", list=" + list + "]";
    }

}
