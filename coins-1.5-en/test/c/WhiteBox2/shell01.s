 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
___sgetc:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%esi
	movl	4(%esi),%eax
	leal	-1(%eax),%eax
	movl	%eax,4(%esi)
	movl	4(%esi),%eax
	cmpl	$0,%eax
	jge	.L4
.L3:
	pushl	%esi
	call	___srget
	movl	%eax,%ebx
	leal	4(%esp),%esp
	jmp	.L5
.L4:
	movl	(%esi),%eax
	movzbl	(%eax),%ebx
	movl	(%esi),%eax
	leal	1(%eax),%eax
	movl	%eax,(%esi)
.L5:
	movswl	12(%esi),%eax
	andl	$16384,%eax
	cmpl	$0,%eax
	je	.L18
.L6:
	cmpl	$13,%ebx
	jne	.L8
.L7:
	movl	$1,%eax
	jmp	.L9
.L8:
	movl	$0,%eax
.L9:
	movw	%ax,%ax
	movswl	%ax,%eax
	cmpl	$0,%eax
	je	.L18
.L10:
	movl	4(%esi),%eax
	leal	-1(%eax),%eax
	movl	%eax,4(%esi)
	movl	4(%esi),%eax
	cmpl	$0,%eax
	jge	.L12
.L11:
	pushl	%esi
	call	___srget
	leal	4(%esp),%esp
	jmp	.L13
.L12:
	movl	(%esi),%eax
	movzbl	(%eax),%eax
	movl	(%esi),%ecx
	leal	1(%ecx),%ecx
	movl	%ecx,(%esi)
.L13:
	cmpl	$10,%eax
	jne	.L15
.L14:
	movl	%eax,%ebx
	jmp	.L18
.L15:
	pushl	%esi
	pushl	%eax
	call	_ungetc
	leal	8(%esp),%esp
.L18:
	movl	%ebx,%eax
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_shellsort
_shellsort:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%ebx
	movl	12(%ebp),%eax
	movl	%eax,-4(%ebp)
	movl	$2,%ecx
	movl	-4(%ebp),%eax
	cdq
	idivl	%ecx
.L22:
	cmpl	$0,%eax
	jle	.L35
.L23:
	movl	%eax,%edx
.L24:
	cmpl	-4(%ebp),%edx
	jge	.L34
.L25:
	movl	%edx,%ecx
	subl	%eax,%ecx
.L27:
	cmpl	$0,%ecx
	jl	.L33
.L28:
	leal	(%ecx,%eax),%esi
	movl	(%ebx,%ecx,4),%edi
	cmpl	(%ebx,%esi,4),%edi
	jle	.L33
.L32:
	movl	(%ebx,%ecx,4),%edi
	leal	(%ecx,%eax),%esi
	movl	(%ebx,%esi,4),%esi
	movl	%esi,(%ebx,%ecx,4)
	leal	(%ecx,%eax),%esi
	movl	%edi,(%ebx,%esi,4)
	subl	%eax,%ecx
	jmp	.L27
.L33:
	leal	1(%edx),%edx
	jmp	.L24
.L34:
	movl	$2,%ecx
	cdq
	idivl	%ecx
	jmp	.L22
.L35:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

	.align	1
_string.18:
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$80,%esp
	pushl	%ebx
	pushl	$11
	call	_srand
	leal	4(%esp),%esp
	movl	$0,%ebx
.L38:
	cmpl	$20,%ebx
	jge	.L40
.L39:
	call	_rand
	movl	%eax,-80(%ebp,%ebx,4)
	leal	1(%ebx),%ebx
	jmp	.L38
.L40:
	pushl	$20
	leal	-80(%ebp),%eax
	pushl	%eax
	call	_shellsort
	leal	8(%esp),%esp
	movl	$0,%ebx
.L41:
	cmpl	$20,%ebx
	jge	.L43
.L42:
	pushl	-80(%ebp,%ebx,4)
	pushl	$_string.18
	call	_printf
	leal	8(%esp),%esp
	leal	1(%ebx),%ebx
	jmp	.L41
.L43:
	popl	%ebx
	leave
	ret

