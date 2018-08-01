/*
 * Preston McIllece's Homework 4
 * 
 * This program creates methods for editing and playing songs
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Melody {
	private Queue<Note> song;

	//constructor that is initialized to a queue of notes
	public Melody(Queue<Note> Song) {
		this.song = Song;
	}
	
	//returns the length of the song in seconds
	public double getTotalDuration() {
		double returnValue = 0;
		double repeatValue = 0;
		Note temporary;
		Note tempQueueTemporary;
		Queue<Note> tempQueue = new LinkedList();
		int count = 0;
		boolean check = false;
		int numberOfTrues = 0;
	
		while (count != song.size()) {
			temporary = song.remove();
			//checks if we need to start/stop the repeated section
			if (temporary.isRepeat()) {
				check = true;
				numberOfTrues++;
			}
			
			//checks if we are in the repeated section
			if (check == true) {
				tempQueue.add(temporary);
				returnValue += temporary.getDuration();
			}
			//if we're not in the repeated section, just add normally
			else 
				returnValue += temporary.getDuration();
			
			
			song.add(temporary);
			count++;
			
			//checks if this is the end true value so that you can "turn the repeated section off"
			if (numberOfTrues % 2 == 0)
				check = false;
		}
		//adds up the duration of the repeated section
		while (tempQueue.size() != 0) {
			tempQueueTemporary = tempQueue.remove();
			repeatValue += tempQueueTemporary.getDuration();
		
		}
		//adds the song plus the repeated section
		returnValue += repeatValue;
		
		return returnValue;
	}
	
	//toString method to print out the song
	public String toString() {
		Note temporary;
		String returnValue = "";
		int count = 0;
		
		while (count != song.size()) {
			temporary = song.remove();
			returnValue += temporary.toString() + "\n";
			song.add(temporary);
			count++;
		}
		
		return returnValue;
	}
	
	//changes the tempo of the song
	public void changeTempo(double tempo) {
		Note temporary;
		double tempDuration;
		int count = 0;
		
		while (count != song.size()) {
			temporary = song.remove();
			tempDuration = temporary.getDuration();
			tempDuration *= tempo;
			temporary.setDuration(tempDuration);		
			song.add(temporary);
			count++;
		}
	}
	
	//reverses the song
	public void reverse() {
		Note temporary;
		Note poppedNote;
		Queue<Note> tempQueue = new LinkedList<Note>();
		Stack<Note> tempStack = new Stack<Note>();
		
		
		while (song.peek() != null) {
			temporary = song.remove();
			tempStack.push(temporary);
		}
		
		while (!tempStack.empty()) {
			poppedNote = tempStack.pop();
			song.add(poppedNote);
		}
	}
	
	//adds a song to the end of your current song
	public void append(Melody other) {
		Note temporary;	
		while (other.song.peek() != null) {
			temporary = other.song.remove();		
			song.add(temporary);
		}
	}
	
	//plays a song
	public void play() {
		Note temporary;
		Queue<Note> tempQueue = new LinkedList();
		double tempDuration;
		Accidental tempAccidental;
		int tempOctave;
		Pitch tempPitch;
		int count = 0;
		boolean check = false;
		int numberOfTrues = 0;
		Note n;
		double d;
		Accidental a;
		int o;
		Pitch p;
		
		
		while (count != song.size()) {
			temporary = song.remove();
			
			tempDuration = temporary.getDuration();
			tempAccidental = temporary.getAccidental();
			tempOctave = temporary.getOctave();
			tempPitch = temporary.getPitch();
			
			//checks if this is the beginning or end of a repeated section
			if (temporary.isRepeat()) {
				check = true;
				numberOfTrues++;
			}
			
			//checks if you are in a repeated section
			if (check == true) {
				tempQueue.add(temporary);
			}

			StdAudio.play(tempDuration, tempPitch, tempOctave, tempAccidental);

			//checks if this is the end of the repeated section
			if (numberOfTrues % 2 == 0) {
				check = false;

				//plays the repeated section
				while (tempQueue.size() > 0) {
					n = tempQueue.remove();

					d = n.getDuration();
					a = n.getAccidental();
					o = n.getOctave();
					p = n.getPitch();

					StdAudio.play(d, p, o, a);
				}
			}

			song.add(temporary);
			count++;
		}
	}
}
