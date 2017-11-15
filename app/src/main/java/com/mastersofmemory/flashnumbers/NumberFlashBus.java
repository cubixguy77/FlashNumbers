package com.mastersofmemory.flashnumbers;

import android.os.Bundle;
import android.util.Log;

import com.mastersofmemory.flashnumbers.toolbar.LifeListener;

import java.util.ArrayList;

public class NumberFlashBus implements NumberFlashGameStateListener, SaveInstanceStateListener, RecallDataChangeListener, LifeListener {

    private static NumberFlashBus instance = null;
    private ArrayList<Object> observers;

    public static GameState gameState;

    private NumberFlashBus() {
        observers = new ArrayList<>();
    }

    /* Returns an instance of NumberFlashBus
     * NumberFlashBus is a Singleton and an instance will be created the first time this method is called
     * The instance will be destroyed on device rotation or on play again, but the static fields will retain their values
     */
    public static NumberFlashBus getBus() {
        if(NumberFlashBus.instance == null) {
            NumberFlashBus.instance = new NumberFlashBus();
        }
        return NumberFlashBus.instance;
    }

    public void subscribe(Object observer) {
        if (observer != null && !observers.contains(observer)) {
            this.observers.add(observer);
        }
    }

    boolean hasObservers() {
        return !observers.isEmpty();
    }

    static void unsubscribeAll() {
        Log.d("ML.NumberFlashBus", "unsubscribeAll()");
        if (NumberFlashBus.instance != null) {
            NumberFlashBus.instance.observers.clear();
            NumberFlashBus.instance = null;
        }
    }

    @Override
    public void onPreMemorization(GameData data) {
        gameState = GameState.PRE_MEMORIZATION;

        for (Object observer : observers) {
            if (observer != null && observer instanceof NumberFlashGameStateListener) {
                ((NumberFlashGameStateListener) observer).onPreMemorization(data);
            }
        }
    }

    @Override
    public void onMemorizationStart() {
        gameState = GameState.MEMORIZATION;

        for (Object observer : observers) {
            if (observer != null && observer instanceof NumberFlashGameStateListener) {
                ((NumberFlashGameStateListener) observer).onMemorizationStart();
            }
        }
    }

    @Override
    public void onRecallStart() {
        gameState = GameState.RECALL;

        for (Object observer : observers) {
            if (observer != null && observer instanceof NumberFlashGameStateListener) {
                ((NumberFlashGameStateListener) observer).onRecallStart();
            }
        }
    }

    @Override
    public void onRecallComplete(NumberFlashResult result) {
        for (Object observer : observers) {
            if (observer != null && observer instanceof NumberFlashGameStateListener) {
                ((NumberFlashGameStateListener) observer).onRecallComplete(result);
            }
        }
    }

    @Override
    public void onGameOver() {
        gameState = GameState.REVIEW;
        for (Object observer : observers) {
            if (observer != null && observer instanceof NumberFlashGameStateListener) {
                ((NumberFlashGameStateListener) observer).onGameOver();
            }
        }
    }

    @Override
    public void onShutdown() {
        for (Object observer : observers) {
            if (observer != null && observer instanceof NumberFlashGameStateListener) {
                ((NumberFlashGameStateListener) observer).onShutdown();
            }
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        for (Object observer : observers) {
            if (observer != null && observer instanceof SaveInstanceStateListener) {
                ((SaveInstanceStateListener) observer).onRestoreInstanceState(inState);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        for (Object observer : observers) {
            if (observer != null && observer instanceof SaveInstanceStateListener) {
                ((SaveInstanceStateListener) observer).onSaveInstanceState(outState);
            }
        }
    }

    @Override
    public void onRecallDataChanged(char[] recallData) {
        for (Object observer : observers) {
            if (observer != null && observer instanceof RecallDataChangeListener) {
                ((RecallDataChangeListener) observer).onRecallDataChanged(recallData);
            }
        }
    }

    @Override
    public void onLifeLost(int livesRemaining) {
        for (Object observer : observers) {
            if (observer != null && observer instanceof LifeListener) {
                ((LifeListener) observer).onLifeLost(livesRemaining);
            }
        }
    }
}
