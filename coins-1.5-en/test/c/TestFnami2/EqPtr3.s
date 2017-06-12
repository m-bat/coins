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
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$20,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$0,%edi
	movl	$0,-4(%ebp)
	movl	$0,-12(%ebp)
	movl	$0,-20(%ebp)
	movl	$_a,%esi
	movl	$_a,%ebx
	movl	$_a,-8(%ebp)
	movl	$_a,-16(%ebp)
	movl	$1,%edx
	cmpl	$_a,%esi
	jne	.L7
.L6:
	movl	$1,%ecx
	jmp	.L8
.L7:
	movl	$0,%ecx
.L8:
	cmpl	$0,%esi
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
	movl	-4(%ebp),%eax
	cmpl	$0,%eax
	jne	.L13
.L12:
	movl	$1,%edx
	jmp	.L14
.L13:
	movl	$0,%edx
.L14:
	cmpl	$_a,%ebx
	jne	.L16
.L15:
	movl	$1,%ecx
	jmp	.L17
.L16:
	movl	$0,%ecx
.L17:
	cmpl	$0,%ebx
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
	movl	-12(%ebp),%eax
	cmpl	$0,%eax
	jne	.L22
.L21:
	movl	$1,%ecx
	jmp	.L23
.L22:
	movl	$0,%ecx
.L23:
	movl	-8(%ebp),%eax
	cmpl	$_a,%eax
	jne	.L25
.L24:
	movl	$1,%edx
	jmp	.L26
.L25:
	movl	$0,%edx
.L26:
	movl	-8(%ebp),%eax
	cmpl	$0,%eax
	jne	.L28
.L27:
	movl	$1,%eax
	jmp	.L29
.L28:
	movl	$0,%eax
.L29:
	pushl	%eax
	pushl	%edx
	pushl	%ecx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
	movl	-20(%ebp),%eax
	cmpl	$0,%eax
	jne	.L31
.L30:
	movl	$1,%ecx
	jmp	.L32
.L31:
	movl	$0,%ecx
.L32:
	movl	-16(%ebp),%eax
	cmpl	$_a,%eax
	jne	.L34
.L33:
	movl	$1,%edx
	jmp	.L35
.L34:
	movl	$0,%edx
.L35:
	movl	-16(%ebp),%eax
	cmpl	$0,%eax
	jne	.L37
.L36:
	movl	$1,%eax
	jmp	.L38
.L37:
	movl	$0,%eax
.L38:
	pushl	%eax
	pushl	%edx
	pushl	%ecx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
	cmpl	$0,%edi
	jne	.L40
.L39:
	movl	$1,%edx
	jmp	.L41
.L40:
	movl	$0,%edx
.L41:
	cmpl	$_a,%esi
	jne	.L43
.L42:
	movl	$1,%ecx
	jmp	.L44
.L43:
	movl	$0,%ecx
.L44:
	cmpl	$0,%esi
	jne	.L46
.L45:
	movl	$1,%eax
	jmp	.L47
.L46:
	movl	$0,%eax
.L47:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%eax
	cmpl	-4(%ebp),%eax
	jne	.L49
.L48:
	movl	$1,%edx
	jmp	.L50
.L49:
	movl	$0,%edx
.L50:
	cmpl	$_a,%ebx
	jne	.L52
.L51:
	movl	$1,%ecx
	jmp	.L53
.L52:
	movl	$0,%ecx
.L53:
	cmpl	$0,%ebx
	jne	.L55
.L54:
	movl	$1,%eax
	jmp	.L56
.L55:
	movl	$0,%eax
.L56:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%eax
	cmpl	-12(%ebp),%eax
	jne	.L58
.L57:
	movl	$1,%edx
	jmp	.L59
.L58:
	movl	$0,%edx
.L59:
	movl	$_a,%eax
	cmpl	-8(%ebp),%eax
	jne	.L61
.L60:
	movl	$1,%ecx
	jmp	.L62
.L61:
	movl	$0,%ecx
.L62:
	movl	$0,%eax
	cmpl	-8(%ebp),%eax
	jne	.L64
.L63:
	movl	$1,%eax
	jmp	.L65
.L64:
	movl	$0,%eax
.L65:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%eax
	cmpl	-20(%ebp),%eax
	jne	.L67
.L66:
	movl	$1,%edx
	jmp	.L68
.L67:
	movl	$0,%edx
.L68:
	movl	$_a,%eax
	cmpl	-16(%ebp),%eax
	jne	.L70
.L69:
	movl	$1,%ecx
	jmp	.L71
.L70:
	movl	$0,%ecx
.L71:
	movl	$0,%eax
	cmpl	-16(%ebp),%eax
	jne	.L73
.L72:
	movl	$1,%eax
	jmp	.L74
.L73:
	movl	$0,%eax
.L74:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_string.35
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

	.comm	_a,40
