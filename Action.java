public class Action
{
	// Water source
	public String from;

    // Water destination
	public String to;

    // Quantity in gallons
	public int quantity;

    public Action(String from, String to, int quantity)
    {
    	this.from = from;
    	this.to = to;
    	this.quantity = quantity;
    }
}