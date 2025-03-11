import globals from "globals";
import pluginReact from "eslint-plugin-react";
import pluginReactHooks from "eslint-plugin-react-hooks";
import pluginReactRefresh from "eslint-plugin-react-refresh";
import tseslint from "typescript-eslint";
import js from "@eslint/js";
import pluginPrettierRecommended from "eslint-plugin-prettier/recommended";
import pluginImport from "eslint-plugin-import";

export default tseslint.config(
  { ignores: ["node_modules", "dist"] },
  js.configs.recommended,
  tseslint.configs.recommended,
  {
    rules: {
      "no-console": "warn",
    },
  },
  {
    extends: [
      pluginImport.flatConfigs.recommended,
      pluginImport.flatConfigs.typescript,
    ],
    files: ["**/*.{ts,tsx}"],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
    },
    settings: {
      "import/resolver": {
        typescript: {},
      },
    },
    rules: {
      "import/order": [
        "error",
        {
          groups: [
            "external",
            "builtin",
            "index",
            ["sibling", "parent"],
            "internal",
            "type"
          ],
          alphabetize: {
            order: "asc",
            caseInsensitive: true,
          },
          "newlines-between": "always-and-inside-groups",
        },
      ],
    },
  },
  {
    plugins: {
      react: pluginReact,
      "react-hooks": pluginReactHooks,
      "react-refresh": pluginReactRefresh,
    },
    rules: {
      ...pluginReactHooks.configs.recommended.rules,
      "react/jsx-props-no-spreading": ["warn", { html: "ignore" }],
      "react-refresh/only-export-components": [
        "warn",
        { allowConstantExport: true },
      ],
    },
  },
  {
    files: ["**/*.{js,jsx,mjs,cjs,ts,tsx}"],
    plugins: {
      react: pluginReact,
    },
    languageOptions: {
      parserOptions: {
        ecmaFeatures: {
          jsx: true,
        },
      },
      globals: {
        ...globals.browser,
      },
    },
  },
  {
    rules: {
      "@typescript-eslint/no-unused-vars": ["error", {
        varsIgnorePattern: "^_",
        argsIgnorePattern: "^_",
      }],
    },
  },
  pluginPrettierRecommended
);
