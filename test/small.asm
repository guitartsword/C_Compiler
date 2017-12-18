.data
_msg0: .asciiz "Ingrese un numero: "
_msg1: .asciiz "El doble de su numero es: %d"
.text
.globl main
main:
move $fp, $sp
sw $fp, -4($sp)
sub $sp, $sp, 4
sub $sp, $sp, 4
la $a0, _msg0
li $v0, 4
syscall
move $t0, $v0
li $v0, 5
syscall
sw $v0, -8($fp)
lw $t0, -8($fp)
mul $t0, $t0, 2
sw $t0, -8($fp)
la $a0, _msg1
lw $t0, -8($fp)
move $a1, $t0
li $v0, 4
syscall
move $a0, $a1
li $v0, 1
syscall
move $t0, $v0
