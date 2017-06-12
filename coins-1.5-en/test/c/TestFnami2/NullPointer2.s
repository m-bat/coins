 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.31:
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	%eax,%ecx
	jne	.L4
.L3:
	movl	$1,%eax
	jmp	.L5
.L4:
	movl	$0,%eax
.L5:
	pushl	%eax
	pushl	$_string.31
	call	_printf
	leal	8(%esp),%esp
.L6:
	leave
	ret

	.align	1
_string.34:
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
	.byte	32
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
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$52,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$0,-52(%ebp)
	movl	$0,-44(%ebp)
	movl	$0,-40(%ebp)
	movl	$0,-36(%ebp)
	movl	$0,-32(%ebp)
	movl	$0,-28(%ebp)
	movl	$0,-24(%ebp)
	movl	$0,-12(%ebp)
	movl	$0,-48(%ebp)
	movl	$1,-16(%ebp)
	movl	-44(%ebp),%eax
	cmpl	$0,%eax
	jne	.L13
.L12:
	movl	$1,-4(%ebp)
	jmp	.L14
.L13:
	movl	$0,-4(%ebp)
.L14:
	movl	-40(%ebp),%eax
	cmpl	$0,%eax
	jne	.L16
.L15:
	movl	$1,%ecx
	jmp	.L17
.L16:
	movl	$0,%ecx
.L17:
	movl	-36(%ebp),%eax
	cmpl	$0,%eax
	jne	.L19
.L18:
	movl	$1,%edx
	jmp	.L20
.L19:
	movl	$0,%edx
.L20:
	movl	-32(%ebp),%eax
	cmpl	$0,%eax
	jne	.L22
.L21:
	movl	$1,%ebx
	jmp	.L23
.L22:
	movl	$0,%ebx
.L23:
	movl	-28(%ebp),%eax
	cmpl	$0,%eax
	jne	.L25
.L24:
	movl	$1,%esi
	jmp	.L26
.L25:
	movl	$0,%esi
.L26:
	movl	-24(%ebp),%eax
	cmpl	$0,%eax
	jne	.L28
.L27:
	movl	$1,%edi
	jmp	.L29
.L28:
	movl	$0,%edi
.L29:
	movl	-12(%ebp),%eax
	cmpl	$0,%eax
	jne	.L31
.L30:
	movl	$1,%eax
	jmp	.L32
.L31:
	movl	$0,%eax
.L32:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-4(%ebp)
	pushl	-16(%ebp)
	pushl	$_string.34
	call	_printf
	leal	36(%esp),%esp
	movl	-52(%ebp),%eax
	cmpl	-48(%ebp),%eax
	jne	.L34
.L33:
	movl	$1,-20(%ebp)
	jmp	.L35
.L34:
	movl	$0,-20(%ebp)
.L35:
	movl	-44(%ebp),%eax
	cmpl	-48(%ebp),%eax
	jne	.L37
.L36:
	movl	$1,-8(%ebp)
	jmp	.L38
.L37:
	movl	$0,-8(%ebp)
.L38:
	movl	-40(%ebp),%eax
	cmpl	-48(%ebp),%eax
	jne	.L40
.L39:
	movl	$1,%ecx
	jmp	.L41
.L40:
	movl	$0,%ecx
.L41:
	movl	-36(%ebp),%eax
	cmpl	-48(%ebp),%eax
	jne	.L43
.L42:
	movl	$1,%edx
	jmp	.L44
.L43:
	movl	$0,%edx
.L44:
	movl	-32(%ebp),%eax
	cmpl	-48(%ebp),%eax
	jne	.L46
.L45:
	movl	$1,%ebx
	jmp	.L47
.L46:
	movl	$0,%ebx
.L47:
	movl	-28(%ebp),%eax
	cmpl	-48(%ebp),%eax
	jne	.L49
.L48:
	movl	$1,%esi
	jmp	.L50
.L49:
	movl	$0,%esi
.L50:
	movl	-24(%ebp),%eax
	cmpl	-48(%ebp),%eax
	jne	.L52
.L51:
	movl	$1,%edi
	jmp	.L53
.L52:
	movl	$0,%edi
.L53:
	movl	-12(%ebp),%eax
	cmpl	-48(%ebp),%eax
	jne	.L55
.L54:
	movl	$1,%eax
	jmp	.L56
.L55:
	movl	$0,%eax
.L56:
	pushl	%eax
	pushl	%edi
	pushl	%esi
	pushl	%ebx
	pushl	%edx
	pushl	%ecx
	pushl	-8(%ebp)
	pushl	-20(%ebp)
	pushl	$_string.34
	call	_printf
	leal	36(%esp),%esp
	movl	-52(%ebp),%eax
	cmpl	-48(%ebp),%eax
	jne	.L58
.L57:
	movl	$1,%eax
	jmp	.L59
.L58:
	movl	$0,%eax
.L59:
	pushl	%eax
	pushl	$_string.31
	call	_printf
	leal	8(%esp),%esp
	pushl	-48(%ebp)
	pushl	-52(%ebp)
	call	_f
	leal	8(%esp),%esp
	movl	$0,%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

