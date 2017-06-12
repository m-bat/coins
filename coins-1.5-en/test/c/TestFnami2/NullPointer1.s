 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
.L2:
	leave
	ret

	.align	1
_string.20:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_g
_g:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%edx
	movl	12(%ebp),%esi
	movl	$0,%ecx
	movl	$0,%eax
	cmpl	$0,%edx
	jne	.L6
.L5:
	movl	$1,%ebx
	jmp	.L7
.L6:
	movl	$0,%ebx
.L7:
	cmpl	%ecx,%edx
	jne	.L9
.L8:
	movl	$1,%edx
	jmp	.L10
.L9:
	movl	$0,%edx
.L10:
	cmpl	$0,%esi
	jne	.L12
.L11:
	movl	$1,%ecx
	jmp	.L13
.L12:
	movl	$0,%ecx
.L13:
	cmpl	%eax,%esi
	jne	.L15
.L14:
	movl	$1,%eax
	jmp	.L16
.L15:
	movl	$0,%eax
.L16:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	$_string.20
	call	_printf
	leal	20(%esp),%esp
.L17:
	popl	%esi
	popl	%ebx
	leave
	ret

	.align	1
_string.22:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_h
_h:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	$_x,%ecx
	jne	.L21
.L20:
	movl	$1,%ecx
	jmp	.L22
.L21:
	movl	$0,%ecx
.L22:
	cmpl	$_f,%eax
	jne	.L24
.L23:
	movl	$1,%eax
	jmp	.L25
.L24:
	movl	$0,%eax
.L25:
	pushl	%eax
	pushl	%ecx
	pushl	$_string.22
	call	_printf
	leal	12(%esp),%esp
.L26:
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$0,%esi
	movl	$0,%ebx
	movl	$_x,%eax
	cmpl	$0,%eax
	jne	.L30
.L29:
	movl	$1,%edx
	jmp	.L31
.L30:
	movl	$0,%edx
.L31:
	cmpl	$_x,%esi
	jne	.L33
.L32:
	movl	$1,%ecx
	jmp	.L34
.L33:
	movl	$0,%ecx
.L34:
	movl	$_f,%eax
	cmpl	$0,%eax
	jne	.L36
.L35:
	movl	$1,%edi
	jmp	.L37
.L36:
	movl	$0,%edi
.L37:
	cmpl	$_f,%ebx
	jne	.L39
.L38:
	movl	$1,%eax
	jmp	.L40
.L39:
	movl	$0,%eax
.L40:
	pushl	%eax
	pushl	%edi
	pushl	%ecx
	pushl	%edx
	pushl	$_string.20
	call	_printf
	leal	20(%esp),%esp
	pushl	$_f
	pushl	$_x
	call	_g
	leal	8(%esp),%esp
	pushl	%ebx
	pushl	%esi
	call	_h
	leal	8(%esp),%esp
	movl	$0,%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

	.comm	_x,4
