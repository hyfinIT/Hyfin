'use strict';

const gulp = require('gulp');
const babel = require('gulp-babel');
const changed = require('gulp-changed');
const concat = require('gulp-concat');
const rename = require("gulp-rename");
const sass = require('gulp-sass');
const uglify = require('gulp-uglify');
const bulkSass = require('gulp-sass-bulk-import');
const include = require('gulp-include')

/**
 * Asset paths.
 */
const scssSrc = 'styles/*.scss';
const assetsDir = '../assets/';

/**
 * SCSS task
 */
gulp.task('css', function () {
  return gulp.src(scssSrc)
    .pipe(bulkSass())
    .pipe(sass())
    .pipe(rename('theme.css'))
    .pipe(gulp.dest(assetsDir))
});

/**
 * Watch task
 */
gulp.task('watch', function () {
  gulp.watch(['styles/*.scss', 'styles/*/*.scss'], gulp.series('css'));
});

/**
 * Default task
 */
gulp.task('default', gulp.series('css') );