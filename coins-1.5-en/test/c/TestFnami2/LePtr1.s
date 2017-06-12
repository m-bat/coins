 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_s,%eax
	jg	.L4
.L3:
	movl	$1,%ecx
	jmp	.L5
.L4:
	movl	$0,%ecx
.L5:
	cmpl	$_s+4,%eax
	jg	.L7
.L6:
	movl	$1,%eax
	jmp	.L8
.L7:
	movl	$0,%eax
.L8:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L9:
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_u,%eax
	jg	.L13
.L12:
	movl	$1,%ecx
	jmp	.L14
.L13:
	movl	$0,%ecx
.L14:
	cmpl	$_u,%eax
	jg	.L16
.L15:
	movl	$1,%eax
	jmp	.L17
.L16:
	movl	$0,%eax
.L17:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L18:
	leave
	ret


	.align	4
	.global	_h0
_h0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_a+8,%eax
	jg	.L22
.L21:
	movl	$1,%ecx
	jmp	.L23
.L22:
	movl	$0,%ecx
.L23:
	cmpl	$_a+16,%eax
	jg	.L25
.L24:
	movl	$1,%eax
	jmp	.L26
.L25:
	movl	$0,%eax
.L26:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L27:
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_s,%eax
	jl	.L31
.L30:
	movl	$1,%ecx
	jmp	.L32
.L31:
	movl	$0,%ecx
.L32:
	cmpl	$_s+4,%eax
	jl	.L34
.L33:
	movl	$1,%eax
	jmp	.L35
.L34:
	movl	$0,%eax
.L35:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L36:
	leave
	ret


	.align	4
	.global	_g1
_g1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_u,%eax
	jl	.L40
.L39:
	movl	$1,%ecx
	jmp	.L41
.L40:
	movl	$0,%ecx
.L41:
	cmpl	$_u,%eax
	jl	.L43
.L42:
	movl	$1,%eax
	jmp	.L44
.L43:
	movl	$0,%eax
.L44:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L45:
	leave
	ret


	.align	4
	.global	_h1
_h1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$_a+8,%eax
	jl	.L49
.L48:
	movl	$1,%ecx
	jmp	.L50
.L49:
	movl	$0,%ecx
.L50:
	cmpl	$_a+16,%eax
	jl	.L52
.L51:
	movl	$1,%eax
	jmp	.L53
.L52:
	movl	$0,%eax
.L53:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L54:
	leave
	ret


	.align	4
	.global	_op
_op:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L58
.L57:
	movl	$1,%eax
	jmp	.L59
.L58:
	movl	$0,%eax
.L59:
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%esi
	pushl	$_s
	pushl	%esi
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$_s+4
	pushl	%esi
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L63:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g2
_g2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%esi
	pushl	$_u
	pushl	%esi
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$_u
	pushl	%esi
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L66:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_h2
_h2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%esi
	pushl	$_a+8
	pushl	%esi
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$_a+16
	pushl	%esi
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L69:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0g
_main0g:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$_s,%eax
	cmpl	$_s,%eax
	jg	.L73
.L72:
	movl	$1,%edx
	jmp	.L74
.L73:
	movl	$0,%edx
.L74:
	movl	$_s,%eax
	cmpl	$_s+4,%eax
	jg	.L76
.L75:
	movl	$1,%ecx
	jmp	.L77
.L76:
	movl	$0,%ecx
.L77:
	movl	$_s+4,%eax
	cmpl	$_s,%eax
	jg	.L79
.L78:
	movl	$1,-28(%ebp)
	jmp	.L80
.L79:
	movl	$0,-28(%ebp)
.L80:
	movl	$_s+4,%eax
	cmpl	$_s+4,%eax
	jg	.L82
.L81:
	movl	$1,-16(%ebp)
	jmp	.L83
.L82:
	movl	$0,-16(%ebp)
.L83:
	movl	$_u,%eax
	cmpl	$_u,%eax
	jg	.L85
.L84:
	movl	$1,-24(%ebp)
	jmp	.L86
.L85:
	movl	$0,-24(%ebp)
.L86:
	movl	$_u,%eax
	cmpl	$_u,%eax
	jg	.L88
.L87:
	movl	$1,-8(%ebp)
	jmp	.L89
.L88:
	movl	$0,-8(%ebp)
.L89:
	movl	$_u,%eax
	cmpl	$_u,%eax
	jg	.L91
.L90:
	movl	$1,-20(%ebp)
	jmp	.L92
.L91:
	movl	$0,-20(%ebp)
.L92:
	movl	$_u,%eax
	cmpl	$_u,%eax
	jg	.L94
.L93:
	movl	$1,%esi
	jmp	.L95
.L94:
	movl	$0,%esi
.L95:
	movl	$_a+8,%eax
	cmpl	$_a+8,%eax
	jg	.L97
.L96:
	movl	$1,-12(%ebp)
	jmp	.L98
.L97:
	movl	$0,-12(%ebp)
.L98:
	movl	$_a+8,%eax
	cmpl	$_a+16,%eax
	jg	.L100
.L99:
	movl	$1,%ebx
	jmp	.L101
.L100:
	movl	$0,%ebx
.L101:
	movl	$_a+16,%eax
	cmpl	$_a+8,%eax
	jg	.L103
.L102:
	movl	$1,-4(%ebp)
	jmp	.L104
.L103:
	movl	$0,-4(%ebp)
.L104:
	movl	$_a+16,%eax
	cmpl	$_a+16,%eax
	jg	.L106
.L105:
	movl	$1,%edi
	jmp	.L107
.L106:
	movl	$0,%edi
.L107:
	pushl	%ecx
	pushl	%edx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	-16(%ebp)
	pushl	-28(%ebp)
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	-8(%ebp)
	pushl	-24(%ebp)
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	%esi
	pushl	-20(%ebp)
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	%ebx
	pushl	-12(%ebp)
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	%edi
	pushl	-4(%ebp)
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L108:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0l
_main0l:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$60,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$0,-28(%ebp)
	movl	$10,-24(%ebp)
	movl	$20,-20(%ebp)
	movl	$30,-16(%ebp)
	leal	-8(%ebp),%ecx
	leal	-8(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L112
.L111:
	movl	$1,-60(%ebp)
	jmp	.L113
.L112:
	movl	$0,-60(%ebp)
.L113:
	leal	-8(%ebp),%ecx
	leal	-4(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L115
.L114:
	movl	$1,%edx
	jmp	.L116
.L115:
	movl	$0,%edx
.L116:
	leal	-4(%ebp),%ecx
	leal	-8(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L118
.L117:
	movl	$1,-56(%ebp)
	jmp	.L119
.L118:
	movl	$0,-56(%ebp)
.L119:
	leal	-4(%ebp),%ecx
	leal	-4(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L121
.L120:
	movl	$1,-48(%ebp)
	jmp	.L122
.L121:
	movl	$0,-48(%ebp)
.L122:
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L124
.L123:
	movl	$1,-52(%ebp)
	jmp	.L125
.L124:
	movl	$0,-52(%ebp)
.L125:
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L127
.L126:
	movl	$1,-40(%ebp)
	jmp	.L128
.L127:
	movl	$0,-40(%ebp)
.L128:
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L130
.L129:
	movl	$1,-44(%ebp)
	jmp	.L131
.L130:
	movl	$0,-44(%ebp)
.L131:
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L133
.L132:
	movl	$1,-32(%ebp)
	jmp	.L134
.L133:
	movl	$0,-32(%ebp)
.L134:
	leal	-20(%ebp),%ecx
	leal	-20(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L136
.L135:
	movl	$1,-36(%ebp)
	jmp	.L137
.L136:
	movl	$0,-36(%ebp)
.L137:
	leal	-20(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L139
.L138:
	movl	$1,%esi
	jmp	.L140
.L139:
	movl	$0,%esi
.L140:
	leal	-12(%ebp),%ecx
	leal	-20(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L142
.L141:
	movl	$1,%ebx
	jmp	.L143
.L142:
	movl	$0,%ebx
.L143:
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	jg	.L145
.L144:
	movl	$1,%edi
	jmp	.L146
.L145:
	movl	$0,%edi
.L146:
	pushl	%edx
	pushl	-60(%ebp)
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	-48(%ebp)
	pushl	-56(%ebp)
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	-40(%ebp)
	pushl	-52(%ebp)
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	-32(%ebp)
	pushl	-44(%ebp)
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	%esi
	pushl	-36(%ebp)
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	%edi
	pushl	%ebx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L147:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main1
_main1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_s,%ebx
	cmpl	$_s,%ebx
	jg	.L151
.L150:
	movl	$1,%ecx
	jmp	.L152
.L151:
	movl	$0,%ecx
.L152:
	cmpl	$_s+4,%ebx
	jg	.L154
.L153:
	movl	$1,%eax
	jmp	.L155
.L154:
	movl	$0,%eax
.L155:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_s,%ebx
	jg	.L157
.L156:
	movl	$1,%ecx
	jmp	.L158
.L157:
	movl	$0,%ecx
.L158:
	cmpl	$_s+4,%ebx
	jg	.L160
.L159:
	movl	$1,%eax
	jmp	.L161
.L160:
	movl	$0,%eax
.L161:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	$_s+4,%ebx
	cmpl	$_s,%ebx
	jg	.L163
.L162:
	movl	$1,%ecx
	jmp	.L164
.L163:
	movl	$0,%ecx
.L164:
	cmpl	$_s+4,%ebx
	jg	.L166
.L165:
	movl	$1,%eax
	jmp	.L167
.L166:
	movl	$0,%eax
.L167:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_s,%ebx
	jg	.L169
.L168:
	movl	$1,%ecx
	jmp	.L170
.L169:
	movl	$0,%ecx
.L170:
	cmpl	$_s+4,%ebx
	jg	.L172
.L171:
	movl	$1,%eax
	jmp	.L173
.L172:
	movl	$0,%eax
.L173:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L174:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main2
_main2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jg	.L178
.L177:
	movl	$1,%ecx
	jmp	.L179
.L178:
	movl	$0,%ecx
.L179:
	cmpl	$_u,%ebx
	jg	.L181
.L180:
	movl	$1,%eax
	jmp	.L182
.L181:
	movl	$0,%eax
.L182:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_u,%ebx
	jg	.L184
.L183:
	movl	$1,%ecx
	jmp	.L185
.L184:
	movl	$0,%ecx
.L185:
	cmpl	$_u,%ebx
	jg	.L187
.L186:
	movl	$1,%eax
	jmp	.L188
.L187:
	movl	$0,%eax
.L188:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jg	.L190
.L189:
	movl	$1,%ecx
	jmp	.L191
.L190:
	movl	$0,%ecx
.L191:
	cmpl	$_u,%ebx
	jg	.L193
.L192:
	movl	$1,%eax
	jmp	.L194
.L193:
	movl	$0,%eax
.L194:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_u,%ebx
	jg	.L196
.L195:
	movl	$1,%ecx
	jmp	.L197
.L196:
	movl	$0,%ecx
.L197:
	cmpl	$_u,%ebx
	jg	.L199
.L198:
	movl	$1,%eax
	jmp	.L200
.L199:
	movl	$0,%eax
.L200:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L201:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main3
_main3:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_a+8,%ebx
	cmpl	$_a+8,%ebx
	jg	.L205
.L204:
	movl	$1,%ecx
	jmp	.L206
.L205:
	movl	$0,%ecx
.L206:
	cmpl	$_a+16,%ebx
	jg	.L208
.L207:
	movl	$1,%eax
	jmp	.L209
.L208:
	movl	$0,%eax
.L209:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_a+8,%ebx
	jg	.L211
.L210:
	movl	$1,%ecx
	jmp	.L212
.L211:
	movl	$0,%ecx
.L212:
	cmpl	$_a+16,%ebx
	jg	.L214
.L213:
	movl	$1,%eax
	jmp	.L215
.L214:
	movl	$0,%eax
.L215:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	$_a+16,%ebx
	cmpl	$_a+8,%ebx
	jg	.L217
.L216:
	movl	$1,%ecx
	jmp	.L218
.L217:
	movl	$0,%ecx
.L218:
	cmpl	$_a+16,%ebx
	jg	.L220
.L219:
	movl	$1,%eax
	jmp	.L221
.L220:
	movl	$0,%eax
.L221:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_a+8,%ebx
	jg	.L223
.L222:
	movl	$1,%ecx
	jmp	.L224
.L223:
	movl	$0,%ecx
.L224:
	cmpl	$_a+16,%ebx
	jg	.L226
.L225:
	movl	$1,%eax
	jmp	.L227
.L226:
	movl	$0,%eax
.L227:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L228:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main4
_main4:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_s,%ebx
	cmpl	$_s,%ebx
	jl	.L232
.L231:
	movl	$1,%ecx
	jmp	.L233
.L232:
	movl	$0,%ecx
.L233:
	cmpl	$_s+4,%ebx
	jl	.L235
.L234:
	movl	$1,%eax
	jmp	.L236
.L235:
	movl	$0,%eax
.L236:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_s,%ebx
	jl	.L238
.L237:
	movl	$1,%ecx
	jmp	.L239
.L238:
	movl	$0,%ecx
.L239:
	cmpl	$_s+4,%ebx
	jl	.L241
.L240:
	movl	$1,%eax
	jmp	.L242
.L241:
	movl	$0,%eax
.L242:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	$_s+4,%ebx
	cmpl	$_s,%ebx
	jl	.L244
.L243:
	movl	$1,%ecx
	jmp	.L245
.L244:
	movl	$0,%ecx
.L245:
	cmpl	$_s+4,%ebx
	jl	.L247
.L246:
	movl	$1,%eax
	jmp	.L248
.L247:
	movl	$0,%eax
.L248:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_s,%ebx
	jl	.L250
.L249:
	movl	$1,%ecx
	jmp	.L251
.L250:
	movl	$0,%ecx
.L251:
	cmpl	$_s+4,%ebx
	jl	.L253
.L252:
	movl	$1,%eax
	jmp	.L254
.L253:
	movl	$0,%eax
.L254:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L255:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main5
_main5:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jl	.L259
.L258:
	movl	$1,%ecx
	jmp	.L260
.L259:
	movl	$0,%ecx
.L260:
	cmpl	$_u,%ebx
	jl	.L262
.L261:
	movl	$1,%eax
	jmp	.L263
.L262:
	movl	$0,%eax
.L263:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_u,%ebx
	jl	.L265
.L264:
	movl	$1,%ecx
	jmp	.L266
.L265:
	movl	$0,%ecx
.L266:
	cmpl	$_u,%ebx
	jl	.L268
.L267:
	movl	$1,%eax
	jmp	.L269
.L268:
	movl	$0,%eax
.L269:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	jl	.L271
.L270:
	movl	$1,%ecx
	jmp	.L272
.L271:
	movl	$0,%ecx
.L272:
	cmpl	$_u,%ebx
	jl	.L274
.L273:
	movl	$1,%eax
	jmp	.L275
.L274:
	movl	$0,%eax
.L275:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_u,%ebx
	jl	.L277
.L276:
	movl	$1,%ecx
	jmp	.L278
.L277:
	movl	$0,%ecx
.L278:
	cmpl	$_u,%ebx
	jl	.L280
.L279:
	movl	$1,%eax
	jmp	.L281
.L280:
	movl	$0,%eax
.L281:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L282:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main6
_main6:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_a+8,%ebx
	cmpl	$_a+8,%ebx
	jl	.L286
.L285:
	movl	$1,%ecx
	jmp	.L287
.L286:
	movl	$0,%ecx
.L287:
	cmpl	$_a+16,%ebx
	jl	.L289
.L288:
	movl	$1,%eax
	jmp	.L290
.L289:
	movl	$0,%eax
.L290:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_a+8,%ebx
	jl	.L292
.L291:
	movl	$1,%ecx
	jmp	.L293
.L292:
	movl	$0,%ecx
.L293:
	cmpl	$_a+16,%ebx
	jl	.L295
.L294:
	movl	$1,%eax
	jmp	.L296
.L295:
	movl	$0,%eax
.L296:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	$_a+16,%ebx
	cmpl	$_a+8,%ebx
	jl	.L298
.L297:
	movl	$1,%ecx
	jmp	.L299
.L298:
	movl	$0,%ecx
.L299:
	cmpl	$_a+16,%ebx
	jl	.L301
.L300:
	movl	$1,%eax
	jmp	.L302
.L301:
	movl	$0,%eax
.L302:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$_a+8,%ebx
	jl	.L304
.L303:
	movl	$1,%ecx
	jmp	.L305
.L304:
	movl	$0,%ecx
.L305:
	cmpl	$_a+16,%ebx
	jl	.L307
.L306:
	movl	$1,%eax
	jmp	.L308
.L307:
	movl	$0,%eax
.L308:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L309:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	call	_main0g
	call	_main0l
	call	_main1
	call	_main2
	call	_main3
	call	_main4
	call	_main5
	call	_main6
	pushl	$_s
	call	_f0
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f0
	leal	4(%esp),%esp
	pushl	$_u
	call	_g0
	leal	4(%esp),%esp
	pushl	$_u
	call	_g0
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h0
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h0
	leal	4(%esp),%esp
	pushl	$_s
	call	_f1
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f1
	leal	4(%esp),%esp
	pushl	$_u
	call	_g1
	leal	4(%esp),%esp
	pushl	$_u
	call	_g1
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h1
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h1
	leal	4(%esp),%esp
	pushl	$_s
	call	_f2
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f2
	leal	4(%esp),%esp
	pushl	$_u
	call	_g2
	leal	4(%esp),%esp
	pushl	$_u
	call	_g2
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h2
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h2
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	1
	.global	_s2
_s2:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	4
	.global	_a
_a:
	.long	0
	.long	10
	.long	20
	.long	30
	.comm	_s,8
	.comm	_u,4
