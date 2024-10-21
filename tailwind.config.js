/** @type {import('tailwindcss').Config} */
module.exports = {
    content: ["./src/**/*.{cljs,cljc,html}"],
    theme: {
	extend: {},
	fontFamily: {
	    logo : ["magallanes", "sans-serif"]
	}
    },
    plugins: [],
}
