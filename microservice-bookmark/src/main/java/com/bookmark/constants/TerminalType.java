/**
 * 
 */
package com.bookmark.constants;

/**
 * @author raul
 *
 */
public enum TerminalType {

	IPTV_TV {
		@Override
		public int getValue() {
			return 1;
		}
	},
	Andorid_TV {
		@Override
		public int getValue() {
			return 2;
		}
	},

	PC {
		@Override
		public int getValue() {
			
			return 3;
		}
	},

	Andorid_PAD{
		@Override
		public int getValue() {
		
			return 4;
		}
	},
	IOS_PAD{
		@Override
		public int getValue() {
			
			return 5;
		}
	},

	Andorid_MP{
		@Override
		public int getValue() {
			
			return 6;
		}
	},

	IOS_MP{
		@Override
		public int getValue() {
			return 7;
		}
	},
	
	NULL {
		@Override
		public int getValue() {
			return 8;
		}
	};

	public abstract int getValue();

}
