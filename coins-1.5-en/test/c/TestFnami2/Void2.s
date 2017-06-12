 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.35:
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
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	movl	12(%ebp),%esi
	movl	16(%ebp),%ebx
	cmpl	$_x,%edi
	jne	.L4
.L3:
	movl	$1,%edx
	jmp	.L5
.L4:
	movl	$0,%edx
.L5:
	cmpl	$_a,%esi
	jne	.L7
.L6:
	movl	$1,%ecx
	jmp	.L8
.L7:
	movl	$0,%ecx
.L8:
	cmpl	_sp0,%ebx
	jne	.L10
.L9:
	movl	$1,%eax
	jmp	.L11
.L10:
	movl	$0,%eax
.L11:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_x,%edi
	jne	.L13
.L12:
	movl	$1,%edx
	jmp	.L14
.L13:
	movl	$0,%edx
.L14:
	cmpl	$_a,%esi
	jne	.L16
.L15:
	movl	$1,%ecx
	jmp	.L17
.L16:
	movl	$0,%ecx
.L17:
	cmpl	_sp0,%ebx
	jne	.L19
.L18:
	movl	$1,%eax
	jmp	.L20
.L19:
	movl	$0,%eax
.L20:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
.L21:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g
_g:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	movl	12(%ebp),%esi
	movl	16(%ebp),%ebx
	cmpl	$_x,%edi
	jne	.L25
.L24:
	movl	$1,%edx
	jmp	.L26
.L25:
	movl	$0,%edx
.L26:
	cmpl	$_a,%esi
	jne	.L28
.L27:
	movl	$1,%ecx
	jmp	.L29
.L28:
	movl	$0,%ecx
.L29:
	cmpl	_sp0,%ebx
	jne	.L31
.L30:
	movl	$1,%eax
	jmp	.L32
.L31:
	movl	$0,%eax
.L32:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_x,%edi
	jne	.L34
.L33:
	movl	$1,%edx
	jmp	.L35
.L34:
	movl	$0,%edx
.L35:
	cmpl	$_a,%esi
	jne	.L37
.L36:
	movl	$1,%ecx
	jmp	.L38
.L37:
	movl	$0,%ecx
.L38:
	cmpl	_sp0,%ebx
	jne	.L40
.L39:
	movl	$1,%eax
	jmp	.L41
.L40:
	movl	$0,%eax
.L41:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
.L42:
	popl	%edi
	popl	%esi
	popl	%ebx
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
	call	_init
	movl	$_x,%edi
	movl	$_a,%esi
	movl	_sp0,%ebx
	cmpl	$_x,%edi
	jne	.L46
.L45:
	movl	$1,%edx
	jmp	.L47
.L46:
	movl	$0,%edx
.L47:
	cmpl	$_a,%esi
	jne	.L49
.L48:
	movl	$1,%ecx
	jmp	.L50
.L49:
	movl	$0,%ecx
.L50:
	cmpl	_sp0,%ebx
	jne	.L52
.L51:
	movl	$1,%eax
	jmp	.L53
.L52:
	movl	$0,%eax
.L53:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
	cmpl	$_x,%edi
	jne	.L55
.L54:
	movl	$1,%edx
	jmp	.L56
.L55:
	movl	$0,%edx
.L56:
	cmpl	$_a,%esi
	jne	.L58
.L57:
	movl	$1,%ecx
	jmp	.L59
.L58:
	movl	$0,%ecx
.L59:
	cmpl	_sp0,%ebx
	jne	.L61
.L60:
	movl	$1,%eax
	jmp	.L62
.L61:
	movl	$0,%eax
.L62:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
	pushl	_sp0
	pushl	$_a
	pushl	$_x
	call	_f
	leal	12(%esp),%esp
	pushl	_sp0
	pushl	$_a
	pushl	$_x
	call	_g
	leal	12(%esp),%esp
	movl	$0,%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_init
_init:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$_s,_sp0
.L66:
	leave
	ret

	.comm	_sp0,4
	.comm	_s,4
	.comm	_a,12
	.comm	_x,4
