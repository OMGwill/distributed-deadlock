//Will Luttmann
//Homework2.java

class RandomSleep
{
	static public void SleepAWhile(int min)
	{
		try
		{
			Thread.sleep(min + (int)(Math.random() * 1000));
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}

class ClientInfo
{
	private int _clientId;
	private String _clientName;

	public ClientInfo(int id, String name)
	{
		setClientId(id);
		setClientName(name);
	}

	public int getClientId()
	{
		return (_clientId);
	}

	public void setClientId(int id)
	{
		_clientId = id;
	}

	public String getClientName()
	{
		return (_clientName);
	}

	public void setClientName(String name)
	{
		_clientName = name;
	}
}

class IdGenerator
{
	private int nextId = 1000;

	public int nextValue()
	{
		int v = nextId;
		nextId += 10;
		return (v);
	}
}

abstract class Task
{
	abstract public String doIt(String s);
}

class UpperCaseTask extends Task
{
	public String doIt(String s)
	{
		if (s == null)
			return ("No string!");
		else
			return s.toUpperCase();
	}
}

class ReplaceTask extends Task
{
	public String doIt(String s)
	{
		if (s == null)
			return ("No string!");
		else
		{
			s = s.replace('o', '0');
			s = s.replace('I', '1');
			return s;
		}
	}
}

class ClientList
{
	static final int MAX_NUM_VALUES = 30;
	private int _numValues;
	private ClientInfo[] _list = new ClientInfo[MAX_NUM_VALUES];
	private IdGenerator id = new IdGenerator();

	public ClientList()
	{
		_numValues = 0;
	}

	public void addClient(String name)
	{
		if (_numValues < MAX_NUM_VALUES)
		{
			_list[_numValues] = new ClientInfo(id.nextValue(), name);
			_numValues++;
		}
	}

	public String getClientName(int i)
	{
		if (i >= 0 && i < _numValues)
			return _list[i].getClientName();
		else
			return null;
	}

	public void setClientName(int i, String s)
	{
		if (i >= 0 && i < _numValues)
			_list[i].setClientName(s);
	}

	public int getSize()
	{
		return _numValues;
	}

	public void showClients()
	{
		for (int i = 0; i < _numValues; i++)
		{
			if (_list[i] == null)
				System.out.println("No ClientInfo object.");
			else
				System.out.format("Client %s has id %d\n", _list[i].getClientName(),
					_list[i].getClientId());
		}
	}
}

class Writer implements Runnable
{
	private ClientList _sourceList;
	private ClientList _destList;
	private Task _task;

	public Writer(ClientList s, ClientList d, Task t)
	{
		_sourceList = s;
		_destList = d;
		_task = t;
	}

	public void run()
	{
		int l = _sourceList.getSize();
		int l2 = _destList.getSize();
		if (l > l2)
			l = l2;
		synchronized (_sourceList)
		{
			synchronized (_destList)
			{
				for (int i = 0; i < l; i++)
				{
					RandomSleep.SleepAWhile(10);
				
				
					_destList.setClientName(i, _task.doIt(_sourceList.getClientName(i)));
				
				}
			}
		}
	}
}

public class Homework2
{

	public static void main(String[] args) 
            throws InterruptedException
	{

		ClientList list1 = new ClientList();
		ClientList list2 = new ClientList();
		String[] names = {"Alice", "Bob", "Charlie", "Douglas", "Edwin",
		"Faith", "Gavin", "Harriet", "Inez", "John", "Karla"};
		for (int i = 0; i < names.length; i++)
		{
			list1.addClient(names[i]);
			list2.addClient(names[i]);
		}

		Writer writer1, writer2;
		Thread thread1, thread2;
		writer1 = new Writer(list1, list2, new UpperCaseTask());
		thread1 = new Thread(writer1);

		writer2 = new Writer(list2, list1, new ReplaceTask());
		thread2 = new Thread(writer2);


		thread1.start();
		thread2.start();
		
		thread2.join();
		thread1.join();

		System.out.println("First list:");
		list1.showClients();
		System.out.println("Second list:");
		list2.showClients();
		System.exit(0);
	}
}
