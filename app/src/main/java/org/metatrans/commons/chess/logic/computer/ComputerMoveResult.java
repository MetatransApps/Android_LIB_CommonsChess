package org.metatrans.commons.chess.logic.computer;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.metatrans.commons.chess.model.Move;


public class ComputerMoveResult implements RunnableFuture<Move> {

	
	private IComputer computerPlayer;
	private Move computerMove;
	private boolean cancelled;
	
	
	public ComputerMoveResult(IComputer _computerPlayer) {
		computerPlayer = _computerPlayer;
	}
	
	
	@Override
	public boolean cancel(boolean arg0) {
		cancelled = true;
		return true;
	}

	@Override
	public Move get() throws InterruptedException, ExecutionException {
		return computerMove;
	}

	@Override
	public Move get(long arg0, TimeUnit arg1) throws InterruptedException,
			ExecutionException, TimeoutException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	@Override
	public boolean isDone() {
		return !isCancelled() && computerMove != null;
	}

	@Override
	public void run() {

		try {

			computerMove = computerPlayer.think();

		} catch (Exception e) {

			e.printStackTrace();

			cancelled = true;
		}
	}
}
