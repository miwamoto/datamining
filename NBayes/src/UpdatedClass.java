package NBC;

public class UpdatedClass {
	private String label;
	private double score;
	
	public UpdatedClass(String l, double s){
		this.label = l;
		this.score = s;
	}
	public void addScore(double s){
		this.score *=s;
	}
	public UpdatedClass(UpdatedClass p)
	{
		setLabel(p.getLabel());
		setScore(p.getScore());
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public int compareTo(UpdatedClass o)
	{
		return (int)Math.signum(score - o.score);
	}
	
	public String toString()
	{
		return label+":"+score;
	}
}
