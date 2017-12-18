.data
_msg0: .asciiz "Ingrese un numero: "
_msg1: .asciiz "%d"
_msg2: .asciiz "a=%d\n"
_msg3: .asciiz "for_a=%d\n"
_msg4: .asciiz "Just as expected\n"
_msg5: .asciiz "WHAT THE HELL\n"
_msg6: .asciiz "a=%d\n"
_msg7: .asciiz "b=%d\n"
_msg8: .asciiz "debugging\n"
_msg9: .asciiz "x=%d"
_msg10: .asciiz "*x=%d"
.text
.globl main
main:
la $a0, _msg0
li $v0, 4
syscall
la $a0, _msg1
move $a1, (a)
jal _scanf
la $a0, _msg2
move $a1, a
li $v0, 4
syscall
move $a0, b
jal _algo
move $a0, a
jal _valor
la $a0, _msg3
move $a1, a
li $v0, 4
syscall
la $a0, _msg4
li $v0, 4
syscall
la $a0, _msg5
li $v0, 4
syscall
la $a0, _msg6
move $a1, a
li $v0, 4
syscall
la $a0, _msg7
move $a1, b
li $v0, 4
syscall
jal _debug
_debug:
la $a0, _msg8
li $v0, 4
syscall
_algo:
la $a0, _msg9
move $a1, x
li $v0, 4
syscall
la $a0, _msg10
move $a1, x
li $v0, 4
syscall
_valor:
