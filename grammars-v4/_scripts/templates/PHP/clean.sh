# Generated from trgen <version>
rm -f *.interp
files=()
<tool_grammar_tuples:{x |
files+=( `java -jar "<antlr_tool_path>" -depend -encoding <antlr_encoding> -Dlanguage=PHP <x.AntlrArgs> <antlr_tool_args:{y | <y> } > <x.GrammarFileName> | awk '{print $1\}' | grep -v ':'` )
} >
for i in ${files[*]}
do
    rm -f $i
done
rm -rf vendor composer.lock
exit 0
