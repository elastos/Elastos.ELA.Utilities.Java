package org.elastos.common;

public class Opcode {
        // Constants
        public static byte PUSH0 = 0x00; // An empty array of bytes is pushed onto the stack.
        public static byte PUSHF = PUSH0;
        public static byte PUSHBYTES1 = 0x01; // 0x01-0x4B The next Opcode bytes is data to be pushed onto the stack
        public static byte PUSHBYTES75 = 0x4B;
        public static byte PUSHDATA1 = 0x4C; // The next byte contains the number of bytes to be pushed onto the stack.
        public static byte PUSHDATA2 = 0x4D; // The next two bytes contain the number of bytes to be pushed onto the stack.
        public static byte PUSHDATA4 = 0x4E; // The next four bytes contain the number of bytes to be pushed onto the stack.
        public static byte PUSHM1 = 0x4F; // The number -1 is pushed onto the stack.
        public static byte PUSH1 = 0x51 ;// The number 1 is pushed onto the stack.
        public static byte PUSHT = PUSH1;
        public static byte PUSH2 = 0x52; // The number 2 is pushed onto the stack.
        public static byte PUSH3 = 0x53; // The number 3 is pushed onto the stack.
        public static byte PUSH4 = 0x54; // The number 4 is pushed onto the stack.
        public static byte PUSH5 = 0x55; // The number 5 is pushed onto the stack.
        public static byte PUSH6 = 0x56; // The number 6 is pushed onto the stack.
        public static byte PUSH7 = 0x57; // The number 7 is pushed onto the stack.
        public static byte PUSH8 = 0x58; // The number 8 is pushed onto the stack.
        public static byte PUSH9 = 0x59 ;// The number 9 is pushed onto the stack.
        public static byte PUSH10 = 0x5A; // The number 10 is pushed onto the stack.
        public static byte PUSH11 = 0x5B; // The number 11 is pushed onto the stack.
        public static byte PUSH12 = 0x5C; // The number 12 is pushed onto the stack.
        public static byte PUSH13 = 0x5D; // The number 13 is pushed onto the stack.
        public static byte PUSH14 = 0x5E; // The number 14 is pushed onto the stack.
        public static byte PUSH15 = 0x5F; // The number 15 is pushed onto the stack.
        public static byte PUSH16 = 0x60; // The number 16 is pushed onto the stack.

        // Flow control
        public static byte NOP = 0x61; // Does nothing.
        public static byte JMP = 0x62;
        public static byte JMPIF = 0x63;
        public static byte JMPIFNOT = 0x64;
        public static byte CALL = 0x65;
        public static byte RET = 0x66;
        public static byte APPCALL = 0x67;
        public static byte SYSCALL = 0x68;
        public static byte TAILCALL = 0x69;

        // Stack
        public static byte DUPFROMALTSTACK = 0x6A;
        public static byte TOALTSTACK = 0x6B; // Puts the input onto the top of the alt stack. Removes it from the main stack.
        public static byte FROMALTSTACK = 0x6C; // Puts the input onto the top of the main stack. Removes it from the alt stack.
        public static byte XDROP = 0x6D;
        public static byte XSWAP = 0x72;
        public static byte XTUCK = 0x73;
        public static byte DEPTH = 0x74;// Puts the number of stack items onto the stack.
        public static byte DROP = 0x75;// Removes the top stack item.
        public static byte DUP = 0x76; // Duplicates the top stack item.
        public static byte NIP = 0x77; // Removes the second-to-top stack item.
        public static byte OVER = 0x78; // Copies the second-to-top stack item to the top.
        public static byte PICK = 0x79; // The item n back in the stack is copied to the top.
        public static byte ROLL = 0x7A; // The item n back in the stack is moved to the top.
        public static byte ROT = 0x7B;// The top three items on the stack are rotated to the left.
        public static byte SWAP = 0x7C; // The top two items on the stack are swapped.
        public static byte TUCK = 0x7D; // The item at the top of the stack is copied and inserted before the second-to-top item.

        // Splice
        public static byte CAT = 0x7E; // Concatenates two strings.
        public static byte SUBSTR = 0x7F; // Returns a section of a string.
        public static byte LEFT = (byte)0x80; // Keeps only characters left of the specified point in a string.
        public static byte RIGHT = (byte)0x81; // Keeps only characters right of the specified point in a string.
        public static byte SIZE = (byte)0x82; // Returns the length of the input string.

        // Bitwise logic
        public static byte INVERT = (byte)0x83; // Flips all of the bits in the input.
        public static byte AND = (byte)0x84; // Boolean and between each bit in the inputs.
        public static byte OR = (byte)0x85;// Boolean or between each bit in the inputs.
        public static byte XOR = (byte)0x86; // Boolean exclusive or between each bit in the inputs.
        public static byte EQUAL = (byte)0x87; // Returns 1 if the inputs are exactly equal, 0 otherwise.
        //byte EQUALVERIFY = (byte)0x88; // Same as EQUAL, but runs VERIFY afterward.
        //byte RESERVED1 = (byte)0x89; // Transaction is invalid unless occuring in an unexecuted IF branch
        //byte RESERVED2 = (byte)0x8A; // Transaction is invalid unless occuring in an unexecuted IF branch

        // Arithmetic
        // Note: Arithmetic inputs are limited to signed 32-bit integers, but may overflow their output.
        public static byte INC = (byte)0x8B; // 1 is added to the input.
        public static byte DEC = (byte)0x8C; // 1 is subtracted from the input.
        public static byte SAL = (byte)0x8D; // The input is multiplied by 2.
        public static byte SAR = (byte)0x8E; // The input is divided by 2.
        public static byte NEGATE = (byte)0x8F; // The sign of the input is flipped.
        public static byte ABS = (byte)0x90; // The input is made positive.
        public static byte NOT = (byte)0x91; // If the input is 0 or 1, it is flipped. Otherwise the output will be 0.
        public static byte NZ = (byte)0x92; // Returns 0 if the input is 0. 1 otherwise.
        public static byte ADD = (byte)0x93; // a is added to b.
        public static byte SUB = (byte)0x94; // b is subtracted from a.
        public static byte MUL = (byte)0x95; // a is multiplied by b.
        public static byte DIV = (byte)0x96; // a is divided by b.
        public static byte MOD = (byte)0x97; // Returns the remainder after dividing a by b.
        public static byte SHL = (byte)0x98; // Shifts a left b bits, preserving sign.
        public static byte SHR = (byte)0x99; // Shifts a right b bits, preserving sign.
        public static byte BOOLAND = (byte)0x9A; // If both a and b are not 0, the output is 1. Otherwise 0.
        public static byte BOOLOR = (byte)0x9B; // If a or b is not 0, the output is 1. Otherwise 0.
        public static byte NUMEQUAL = (byte)0x9C; // Returns 1 if the numbers are equal, 0 otherwise.
        public static byte NUMNOTEQUAL = (byte)0x9E; // Returns 1 if the numbers are not equal, 0 otherwise.
        public static byte LT = (byte)0x9F; // Returns 1 if a is less than b, 0 otherwise.
        public static byte GT = (byte)0xA0; // Returns 1 if a is greater than b, 0 otherwise.
        public static byte LTE = (byte)0xA1; // Returns 1 if a is less than or equal to b, 0 otherwise.
        public static byte GTE = (byte)0xA2; // Returns 1 if a is greater than or equal to b, 0 otherwise.
        public static byte MIN = (byte)0xA3; // Returns the smaller of a and b.
        public static byte MAX = (byte)0xA4; // Returns the larger of a and b.
        public static byte WITHIN = (byte)0xA5; // Returns 1 if x is within the specified range (left-inclusive), 0 otherwise.

        // Crypto
        //byte RIPEMD160 = 0xA6; // The input is hashed using RIPEMD-160.
        public static byte SHA1 = (byte)0xA7; // The input is hashed using SHA-1.
        public static byte SHA256 = (byte)0xA8; // The input is hashed using SHA-256.
        public static byte HASH160 = (byte)0xA9;
        public static byte HASH256 = (byte)0xAA;
        public static byte CHECKSIG = (byte)0xAC; // The entire transaction's outputs inputs and script (from the most recently-executed CODESEPARATOR to the end) are hashed. The signature used by CHECKSIG must be a valid signature for this hash and public key. If it is 1 is returned 0 otherwise.
        public static byte CHECKREGID = (byte)0xAD;
        public static byte CHECKMULTISIG = (byte)0xAE; // For each signature and public key pair CHECKSIG is executed. If more public keys than signatures are listed some key/sig pairs can fail. All signatures need to match a public key. If all signatures are valid 1 is returned 0 otherwise. Due to a bug one extra unused value is removed from the stack.

        public static byte CHECKLOCKTIMEVERIFY = (byte)0xb1;
        public static byte CHECKSEQUENCEVERIFY = (byte)0xb2;

        // Array
        public static byte ARRAYSIZE = (byte)0xC0;
        public static byte PACK = (byte)0xC1;
        public static byte UNPACK = (byte)0xC2;
        public static byte PICKITEM = (byte)0xC3;
        public static byte SETITEM = (byte)0xC4;
        public static byte NEWARRAY = (byte)0xC5;

        public static byte NEWSTRUCT = (byte)0xC6;

        // Map
        public static byte NEWMAP = (byte)0xC7;
        public static byte APPEND = (byte)0xC8;
        public static byte REVERSE = (byte)0xC9;
        public static byte REMOVE = (byte)0xCA;
        public static byte HASKEY = (byte)0xCB;
        public static byte KEYS = (byte)0xCC;
        public static byte VALUES = (byte)0xCD;

        //Stack isolation
        public static byte CALL_I = (byte)0xE0;
        public static byte CALL_E = (byte)0xE1;
        public static byte CALL_ED = (byte)0xE2;
        public static byte CALL_ET = (byte)0xE3;
        public static byte CALL_EDT = (byte)0xE4;
}
